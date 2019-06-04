var fakeUsers = [];
var jsonSelect;

var validProperties = [
  'employeeHsaId',
  'intygsadminRole',
  'name'
];

$(document).ready(function() {
  fetchFakeUsers();

  jsonSelect = $("#jsonSelect");

  jsonSelect.change(function() {
    updateUserContext(jsonSelect.val());
  });

  jsonSelect.dblclick(function() {
    updateUserContext(jsonSelect.val());
    $("#loginForm").submit();
  });
});

function fetchFakeUsers() {
  $.getJSON('/fake-api/users')
    .then(
      function(response) {
        fakeUsers = response;
        updateUserList();
      },
      function(data, status) {
        console.log('error ' + status);
      }
    );
}

function updateUserList() {
  jsonSelect.innerHtml = "";

  fakeUsers.forEach(function(item, index) {
    jsonSelect.append(
      '<option id="' + item.employeeHsaId + '" value="' + index + '">' + item.name + ' ' + item.intygsadminRole +
      '</option>');
  });

  jsonSelect.val(0);
  updateUserContext(0);
}

function _replacer(key, value) {
  if (value === null || ($.isArray(value) && value.length === 0)) {
    return undefined;
  }
  return value;
}

function _stringify(fakeUser) {
  var string = JSON.stringify(fakeUser, validProperties, 1);
  var object = JSON.parse(string);

  return JSON.stringify(object, _replacer, 1);
}

function updateUserContext(newSelected, oldVal) {
  if (newSelected === undefined) {
    return;
  }

  // Catch user login option
  var login = fakeUsers[newSelected];

  if (typeof login !== 'undefined') {

    var loginJson = _stringify(login);

    $("#userJsonDisplay").val(loginJson);
  }
}
