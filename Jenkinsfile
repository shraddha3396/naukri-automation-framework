pipeline {
  agent any

  options {
    // Fail early if the branch isn't allowed
    skipDefaultCheckout()
  }

  parameters {
    choice(name: 'TEST_GROUP', choices: ['smoke', 'regression', 'performance', 'security', 'accessibility'], description: 'TestNG group to execute')
    choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to use for web tests')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Validate Branch') {
      when {
        not {
          anyOf {
            branch 'main'
            branch 'develop'
          }
        }
      }
      steps {
        error 'This Jenkins pipeline is configured to run only on main or develop branches.'
      }
    }

    stage('Build & Test') {
      when {
        anyOf {
          branch 'main'
          branch 'develop'
        }
      }
      steps {
        script {
          def command = "mvn clean test -Dgroups=${params.TEST_GROUP} -Dheadless=${params.HEADLESS}"
          if (params.TEST_GROUP == 'smoke' || params.TEST_GROUP == 'regression') {
            command += " -Dbrowser=${params.BROWSER}"
          }

          if (isUnix()) {
            sh command
          } else {
            bat command
          }
        }
      }
    }

    stage('Archive Reports') {
      when {
        anyOf {
          branch 'main'
          branch 'develop'
        }
      }
      steps {
        archiveArtifacts artifacts: 'target/surefire-reports/**, reports/**, screenshots/**, logs/**', allowEmptyArchive: true
      }
    }

    stage('Publish Test Results') {
      when {
        anyOf {
          branch 'main'
          branch 'develop'
        }
      }
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      }
    }
  }

  post {
    failure {
      emailext(
        subject: "❌ Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
        body: """
          <h2>Build Failure Details</h2>
          <p><strong>Job:</strong> ${env.JOB_NAME}</p>
          <p><strong>Build:</strong> ${env.BUILD_NUMBER}</p>
          <p><strong>Branch:</strong> ${env.BRANCH_NAME}</p>
          <p><strong>Commit:</strong> ${env.GIT_COMMIT?.take(7)}</p>
          <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
          <p><strong>Log:</strong> <a href="${env.BUILD_URL}console">View Console Output</a></p>
          <p><strong>Reports:</strong> <a href="${env.BUILD_URL}testReport">View Test Results</a></p>
        """,
        to: '${DEFAULT_RECIPIENTS}',
        mimeType: 'text/html'
      )
    }
    
    success {
      emailext(
        subject: "✅ Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
        body: """
          <h2>Build Success</h2>
          <p><strong>Job:</strong> ${env.JOB_NAME}</p>
          <p><strong>Build:</strong> ${env.BUILD_NUMBER}</p>
          <p><strong>Branch:</strong> ${env.BRANCH_NAME}</p>
          <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
          <p><strong>Reports:</strong> <a href="${env.BUILD_URL}testReport">View Test Results</a></p>
        """,
        to: '${DEFAULT_RECIPIENTS}',
        mimeType: 'text/html'
      )
    }
    
    always {
      cleanWs()
    }
  }
}