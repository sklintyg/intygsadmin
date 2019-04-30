#!groovy

node {
    def buildVersion = "0.0.1.${BUILD_NUMBER}"
    def infraVersion = "3.10.0.+"

    def java11tool = tool name: 'jdk11', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
    def javaHome= "${java11tool}/jdk-11.0.2+9"

    def versionFlags = "-Dorg.gradle.java.home=${javaHome} -DbuildVersion=${buildVersion} -DinfraVersion=${infraVersion}"

    stage('checkout') {
        git url: "https://github.com/sklintyg/intygsadmin.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }

    stage('build') {
        try {
            shgradle "--refresh-dependencies clean build -P client ${versionFlags}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                    reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }

    stage('tag') {
        shgradle "tagRelease ${versionFlags}"
    }

    stage('propagate') {
        gitRef = "v${buildVersion}"
        releaseFlag = "${GIT_BRANCH.startsWith("release")}"
        build job: "intygsadmin-dintyg-build", wait: false, parameters: [
                [$class: 'StringParameterValue', name: 'BUILD_VERSION', value: buildVersion],
                [$class: 'StringParameterValue', name: 'INFRA_VERSION', value: infraVersion],
                [$class: 'StringParameterValue', name: 'GIT_REF', value: gitRef],
                [$class: 'StringParameterValue', name: 'RELEASE_FLAG', value: releaseFlag]
        ]
    }

    stage('notify') {
        util.notifySuccess()
    }
}
