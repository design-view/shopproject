pipeline {
  agent any

  tools {
    jdk "Jdk17"
    gradle "Ga"
  }
  environment { 
    DOCKERHUB_CREDENTIALS = credentials('dockerhuyCre') 
    REGION = "ap-northeast-2"
    AWS_CREDENTIAL_NAME = 'awsTeam4'
  }
  
  stages {
    stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'gitToken',
                    url: 'https://github.com/design-view/springproject.git'
            }
        }
    stage('Git Clone') {
      steps {
        echo 'Git Clone'
        git url: 'https://github.com/design-view/springproject.git',
          branch: 'main', credentialsId: 'gitToken'
      }
    }
    stage('Gradle Build') {
      steps {
        echo 'Gradle Build'
        sh 'chmod +x ./gradlew' // 실행 권한 부여
        sh './gradlew build -x test'
      }
    }
    stage('Docker Image Build') {
      steps {
        echo 'Docker Image Build'                
        dir("${env.WORKSPACE}") {
          sh """
            docker build -t pinkcandy02/springproject:$BUILD_NUMBER .
            docker tag pinkcandy02/springproject:$BUILD_NUMBER pinkcandy02/springproject:latest
            """
        }
      }
    }
    stage('Docker Login') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Docker Image Push') {
      steps {
        echo 'Docker Image Push'  
        sh "docker push pinkcandy02/springproject:latest"
      }
    }
    stage('Cleaning up') { 
      steps { 
        echo 'Cleaning up unused Docker images on Jenkins server'
        sh """
           docker rmi pinkcandy02/springproject:$BUILD_NUMBER
           docker rmi pinkcandy02/springproject:latest
           """
      }
    } 
    stage('Upload S3') {
      steps {
        echo "Upload to S3" 
        dir("${env.WORKSPACE}") {
          sh 'zip -r deploy.zip ./deploy appspec.yml'
          withAWS(region:"${REGION}", credentials: "${AWS_CREDENTIAL_NAME}"){
            s3Upload(file:"deploy.zip", bucket:"team4-min-test-s3")
          }
          sh 'rm -rf ./deploy.zip'
        }
      }
    }
    stage('Codedeploy Workload') {
      steps {
               echo "create Codedeploy group"   
                sh '''
                    aws deploy create-deployment-group \
                    --application-name team4-min-test-codedeploy \
                    --auto-scaling-groups team4-asg-test \
                    --deployment-group-name team4-min-test-codedeploy-${BUILD_NUMBER} \
                    --deployment-config-name CodeDeployDefault.OneAtATime \
                    --service-role-arn arn:aws:iam::491085389788:role/team4-min-test-codedeploy
                    '''
                echo "Codedeploy Workload"   
                sh '''
                    aws deploy create-deployment --application-name team4-min-test-codedeploy \
                    --deployment-config-name CodeDeployDefault.OneAtATime \
                    --deployment-group-name team4-min-test-codedeploy-${BUILD_NUMBER} \
                    --s3-location bucket=team4-min-test-s3,bundleType=zip,key=deploy.zip
                    '''
                    sleep(10) // sleep 10s
            }
        }

  }
}
      
  
