// import {fakeLogin} from "../../api/userApi";

var fakeUsers = [];
var fakeUsersMap = {};
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
    fakeUsersMap[item.employeeHsaId] = item;
    jsonSelect.append(
      '<option id="' + item.employeeHsaId + '" value="' + item.employeeHsaId + '">' + item.name + ' ' + item.intygsadminRole +
      '</option>');
  });

  jsonSelect.val(fakeUsers[0].employeeHsaId);
  updateUserContext(fakeUsers[0].employeeHsaId);
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

function updateUserContext(newSelected) {

  if (newSelected === undefined) {
    return;
  }

  // Catch user login option
  var login = fakeUsersMap[newSelected];

  if (typeof login !== 'undefined') {

    var loginJson = _stringify(login);

    $.getJSON('/fake-api/login', loginJson)
    .then(
      function(response) {
        fakeUsers = response;
        updateUserList();
      },
      function(data, status) {
        console.log('error ' + status);
      }
    );


    //var user = fakeLogin(loginJson);

    // $("#userJsonDisplay").val(loginJson);
  }
}
