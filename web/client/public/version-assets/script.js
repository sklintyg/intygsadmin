let versionInfo

$(document).ready(function() {
  versionInfo = $("#versionInfo")
  updateVersionInfo();
});

function updateVersionInfo() {
  versionInfo.innerHtml = "";

  $.getJSON("/public-api/version")
  .then(
    function(data, status) {
      versionInfo.append('<h3>' + data.applicationName + '</h3>')
      versionInfo.append('<h4>Configuration info</h4>')
      versionInfo.append('Application version: <span class="label label-info">' + data.buildVersion + '</span><br/>')
      versionInfo.append('Build time: <span class="label label-info">' + data.buildTimestamp + '</span><br/>')
      versionInfo.append('Active Profiles: <span class="label label-info">' + data.activeProfiles + '</span>')
    }
  )
}



