#!groovy

node {
    def buildVersion = "1.1.0.${BUILD_NUMBER}-nightly"
    def infraVersion = "3.11.0.+"

    def versionFlags = "-DbuildVersion=${buildVersion} -DinfraVersion=${infraVersion}"

    stage('checkout') {
        git url: "https://github.com/sklintyg/intygsadmin.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }

    stage('owasp') {
        environment {
            JAVA_TOOL_OPTIONS = '-Dfile.encoding=UTF-8'
        }

        try {
            shgradle11 "--refresh-dependencies clean dependencyCheckAggregate ${versionFlags}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports', \
            reportFiles: 'dependency-check-report.html', reportName: 'OWASP dependency-check'
        }
    }

    stage('sonarqube') {
        environment {
            JAVA_TOOL_OPTIONS = '-Dfile.encoding=UTF-8'
        }

        shgradle11 "sonarqube ${versionFlags}"
    }
}
