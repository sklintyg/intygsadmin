var persons = [];
var loginOptions = [];
var medarbetaruppdrag = [];
var appIdentity = "IB";
var jsonSelect;

var validProperties = [
  'hsaId',
  'forNamn',
  'efterNamn',
  'enhetId',
  'legitimeradeYrkesgrupper',
  'systemRoles',
  'befattningsKod',
  'forskrivarKod'
];

$( document ).ready(function() {
  fetchHsaPersons();
  fetchMedarbetaruppdrag();

  jsonSelect = $("#jsonSelect");

  jsonSelect.change(function() {
    updateUserContext(jsonSelect.val());
  });

  jsonSelect.dblclick(function() {
    updateUserContext(jsonSelect.val());
    $("#loginForm").submit();
  });
});

function fetchHsaPersons() {
  $.getJSON('/services/api/hsa-api/person')
    .then(
      function(response) {
        persons = _loginModel(response);
        loginOptions = _loginOptions();
        updateUserList();
      },
      function(data, status) {
        console.log('error ' + status);
      }
    );
}

function fetchMedarbetaruppdrag() {
  $.getJSON('/services/api/hsa-api/medarbetaruppdrag')
    .then(
      function(response) {
        medarbetaruppdrag = response;
      },
      function(data, status) {
        console.log('error ' + status);
      }
    );
}

function updateUserList() {
  jsonSelect.innerHtml = "";

  loginOptions.forEach(function(item, index) {
    jsonSelect.append('<option id="' + item.hsaId + '_' + item.forvaldEnhet + '" value="' + item.index + '">' + item.beskrivning + '</option>');
  });

  jsonSelect.val(0);
  updateUserContext(0);
}

function _getMedarbetaruppdrag(hsaId) {
  return medarbetaruppdrag.filter(function(item) {
    return item.hsaId === hsaId;
  })[0];
}

function _getSystemRoles(hsaId) {
  var mu = _getMedarbetaruppdrag(hsaId);
  if (!mu || !mu.uppdrag) {
    return [];
  }

  return mu.uppdrag.reduce(function(acc, val) {
    if (val.systemRoles) {
      return acc.concat(val.systemRoles);
    }
  }, []);
}


function _filterLoginIdentity(allowedApps, appName) {
  if (!$.isArray(allowedApps)) {
      return false;
  }

  // Allow if array is empty OR app's name is in array
  return allowedApps.length === 0 || allowedApps.indexOf(appName) > -1;
}

function _loginModel(data) {
  if ($.isArray(data)) {
    return data
      .filter(function(item) {
        // Remove all entries where
        //  - fakeProperties are not present
        if (!item.fakeProperties) {
          return false;
        }
        //  - identity allowed in applications are false
        return _filterLoginIdentity(item.fakeProperties.allowedInApplications, appIdentity);
      })
      .sort(function(a, b) {
        // Sort entries by displayOrder
        var value = parseInt(a.fakeProperties.displayOrder, 10) - parseInt(b.fakeProperties.displayOrder, 10);
        if (isNaN(value)) {
          return 1000;
        }
        return value;
      });
  }

  return [];
}

/*
* This method creates the available login options by
* reducing the original HSA-person array to a new array.
* Each HSA-person is on the form:
*
*  {
*    hsaId: "TST5565594230-106J",
*    forNamn: "Markus",
*    efterNamn: "Gran",
*    specialiseringar: [],
*    enhetIds: [],
*    titel: "Läkare",
*    legitimeradeYrkesgrupper: [],
*    befattningsKod: null,
*    forskrivarKod: null,
*    fakeProperties: {
*        allowedInApplications: [],
*        displayOrder: "320",
*        env: "dev",
*        readOnly: true,
*        logins: [
*          {
*            "forvaldEnhet":"IFV1239877878-103D",
*            "beskrivning":"Markus Gran (Läkare | VårdEnhetA)"
*          }
*        ]
*     }
*  }
*
* The reduced login option will be on the form:
*
*  {
*    beskrivning: "Markus Gran (Läkare | VårdEnhetA)"
*    forvaldEnhet: "IFV1239877878-103D"
*    hsaId: "TST5565594230-106J"
*    index: "11"
*  }
*/
function _loginOptions() {
  var index = 0;
  return persons
    .reduce(
      (function(outerProp, innerProp) {

      // Reduce executes the callback function once for each element
      // present in the array, excluding holes in the array, receiving
      // four arguments:
      //   * accumulator
      //   * currentValue
      //   * currentIndex
      //   * array

      var f = function(acc, val, ind, arr) {
        if (val[outerProp] && val[outerProp][innerProp]) {
          val[outerProp][innerProp]
            .forEach(function(obj) {
              // Add properties to object
              obj.index = ''+index++;
              obj.hsaId = val.hsaId;

            });
          acc.push(val[outerProp][innerProp]);
        }
        return acc;
      };
      return f;

    })('fakeProperties', 'logins'), [])
  .reduce(
    function(a, b) {
      return a.concat(b);
    },[]);
}

function _findHsaPerson(hsaId) {
  return persons.filter(function(item) {
    return item.hsaId === hsaId;
  })[0];
}

function _updateObject(object, propertyKey, propertyValue) {
  object[propertyKey] = propertyValue;
}

function _replacer(key, value) {
  if (value === null || ($.isArray(value) && value.length === 0)) {
    return undefined;
  }
  return value;
}

function _stringify(hsaPerson) {
  var string = JSON.stringify(hsaPerson, validProperties, 1);
  var object = JSON.parse(string);

  return JSON.stringify(object, _replacer, 1);
}

function updateUserContext(newSelected, oldVal) {
  if (newSelected === undefined) {
      return;
  }

  // Catch user login option
  var login = loginOptions[newSelected];

  if (typeof login !== 'undefined') {
    // Hantera som privatlakare.
    // Get the HSA person from model
    var hsaPerson = _findHsaPerson(login.hsaId);
    var systemRoles =  _getSystemRoles(login.hsaId);

    // Add properties to HSA person
    _updateObject(hsaPerson, 'enhetId', login.forvaldEnhet);
    _updateObject(hsaPerson, 'systemRoles', systemRoles);

    var loginJson = _stringify(hsaPerson);

    $("#userJsonDisplay").val(loginJson);
  }
}
