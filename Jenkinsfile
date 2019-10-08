#!groovy

def buildVersion = "1.1.0.${BUILD_NUMBER}-nightly"
def infraVersion = "3.11.0.+"

def versionFlags = "-DbuildVersion=${buildVersion} -DinfraVersion=${infraVersion}"

stage('checkout') {
    node {
        git url: "https://github.com/sklintyg/intygsadmin.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }
}

stage('owasp') {
    node {
        try {
            shgradle11 "--refresh-dependencies clean dependencyCheckAggregate ${versionFlags}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports',
                    reportFiles: 'dependency-check-report.html', reportName: 'OWASP dependency-check'
        }
    }
}

stage('sonarqube') {
    node {
        shgradle11 "sonarqube ${versionFlags}"
    }
}
