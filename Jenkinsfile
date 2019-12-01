pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /tmp/.m2:/tmp/.m2'
        }
    }
    options {
        skipStagesAfterUnstable()
    }
	stages {
        stage('Build') {
            parallel {
                stage('Users-API') {
                    steps {
                        dir("users-api") {
                            sh 'mvn -B -DskipTests clean package'
                        }
                    }
                }
            }
        }
        stage('Test') {
            parallel {
                stage('Users-API') {
                    steps {
                        dir("users-api") {
                            sh 'mvn test'
                        }
                    }
                    post {
                        always { 
                            dir("users-api") { 
                                junit 'target/surefire-reports/*.xml'
                            }
                        }
                    }
                }
            }
        }
        stage('Coverage') {
            parallel {
                stage('Users-API') {
                    steps {
                        dir("users-api") {
                            sh 'mvn clean package jacoco:report'
                        }
                    }
                    post {
                        always {
                            dir("users-api") {
                                publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]
                            }
                        }
                    }
                }
            }
        }
	}
}