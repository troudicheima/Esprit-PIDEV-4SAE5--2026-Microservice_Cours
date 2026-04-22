pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('jenkins-sonarr')
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
                bat 'mvn clean test'
                echo "✅ Tests du cours-service passés"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    bat 'mvn sonar:sonar -Dsonar.projectKey=cours-service -Dsonar.projectName="Cours Service" -Dsonar.login=%SONAR_TOKEN%'
                }
                echo "✅ Analyse SonarQube terminée"
            }
        }

        // Suppression de l'étape Quality Gate
    }

    post {
        success {
            echo "🎉 Pipeline CI terminé avec succès pour le cours-service !"
            echo "📊 Voir les résultats sur: ${SONAR_HOST_URL}"
        }
        failure {
            echo "❌ Pipeline échoué"
            echo "Vérifie les tests unitaires ou la qualité du code"
        }
    }
}