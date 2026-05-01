pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('jenkins-sonarrr')
        SERVICE_NAME = 'cours-service'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
        CONTAINER_NAME = "cours-service-${BUILD_NUMBER}"
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

        stage('Create and Run Container') {
            steps {
                echo "🚀 Création et démarrage du conteneur ${CONTAINER_NAME}..."
                bat """
                    docker run -d --name ${CONTAINER_NAME} -p 5059:5059 wallstreet/${SERVICE_NAME}:${BUILD_NUMBER}
                    echo "✅ Conteneur ${CONTAINER_NAME} démarré sur le port 5059"
                """
            }
        }
    }

    post {
        success {
            echo """
🎉 Pipeline CI terminé avec succès pour ${SERVICE_NAME} !
📊 Voir les résultats SonarQube sur : ${SONAR_HOST_URL}/dashboard?id=cours-service
🐳 Image Docker : wallstreet/${SERVICE_NAME}:${BUILD_NUMBER}
🚢 Conteneur "${CONTAINER_NAME}" actif sur le port 5059
"""
        }
        failure {
            echo "❌ Pipeline échoué"
            echo "Vérifie les tests unitaires ou la qualité du code"
        }
        always {
            // Nettoyage : arrêter et supprimer le conteneur, même si le pipeline échoue
            echo "🧹 Nettoyage du conteneur ${CONTAINER_NAME}..."
            bat """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true
            """
            echo "✅ Conteneur supprimé"
        }
    }
}