let fakeUsers = [];
let jsonSelect;

const fakeUsersMap = {};
const fakeLoginUri = "/fake-api/login"
const validProperties = ['employeeHsaId', 'intygsadminRole', 'name'];

$(function() {
  fetchFakeUsers();

  jsonSelect = $("#jsonSelect");

  jsonSelect.change(function() {
    updateUserContext(jsonSelect.val());
  });

  jsonSelect.dblclick(function() {
    updateUserContext(jsonSelect.val());
    fakeLogin()
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

  fakeUsers.forEach(function(item) {
    fakeUsersMap[item.employeeHsaId] = item;
    jsonSelect.append(
      '<option id="' + item.employeeHsaId + '" value="' + item.employeeHsaId + '">' + item.name + ' ' + item.intygsadminRole +
      '</option>');
  });

  jsonSelect.val(fakeUsers[0].employeeHsaId);
  updateUserContext(fakeUsers[0].employeeHsaId);
}

function _replacer(key, value) {
  if (value === null || (Array.isArray(value) && value.length === 0)) {
    return undefined;
  }
  return value;
}

function _stringify(fakeUser) {
  const string = JSON.stringify(fakeUser, validProperties, 1);
  const object = JSON.parse(string);

  return JSON.stringify(object, _replacer, 1);
}

function updateUserContext(newSelected) {
  if (newSelected === undefined) {
    return;
  }

  // Catch user login option
  const login = fakeUsersMap[newSelected];

  if (typeof login !== 'undefined') {
    const loginJson = _stringify(login);
    $("#userJsonDisplay").val(loginJson);
  }
}

function fakeLogin() {
  const userJsonDisplay = $("#userJsonDisplay").val()
  const jsonUser = JSON.stringify(JSON.parse(userJsonDisplay), validProperties)

  fetch(fakeLoginUri, {
    method: 'post',
    headers: {'Content-Type': 'application/json'},
    body: jsonUser
  }).then((response) => {
    if (response.status === 200) {
      window.location.href = '/'
    } else if (response.status === 403) {
      window.location.href = '/#/loggedout/LOGIN_FEL002'
    } else {
      window.location.href = '/#/loggedout/LOGIN_FEL001'
    }
  });
}
