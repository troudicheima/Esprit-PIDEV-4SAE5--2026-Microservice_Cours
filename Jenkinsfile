pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('jenkins-sonarrr')
        SERVICE_NAME = 'cours-service'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "✅ Code récupéré depuis GitHub"
            }
        }

        stage('Tests - Cours Service') {
            steps {
                // Utiliser 'verify' pour générer JaCoCo et les rapports
                bat 'mvn clean verify'
                echo "✅ Tests du cours-service passés"
            }
            post {
                always {
                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    bat """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=cours-service \
                          -Dsonar.projectName="Cours Service" \
                          -Dsonar.login=%SONAR_TOKEN% \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
                echo "✅ Analyse SonarQube terminée"
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Construction de l'image Docker pour ${SERVICE_NAME}..."
                bat """
                    docker build -t wallstreet/${SERVICE_NAME}:${BUILD_NUMBER} \
                                 -t wallstreet/${SERVICE_NAME}:latest .
                """
                echo "✅ Image prête : wallstreet/${SERVICE_NAME}:${BUILD_NUMBER}"
            }
        }
    }

    post {
        success {
            echo """
🎉 Pipeline CI terminé avec succès pour ${SERVICE_NAME} !
📊 Voir les résultats SonarQube sur : ${SONAR_HOST_URL}/dashboard?id=cours-service
🐳 Image Docker : wallstreet/${SERVICE_NAME}:${BUILD_NUMBER}
"""
        }
        failure {
            echo "❌ Pipeline échoué"
            echo "Vérifie les tests unitaires ou la qualité du code"
        }
    }
}