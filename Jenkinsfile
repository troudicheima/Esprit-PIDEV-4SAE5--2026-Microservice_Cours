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

        // PAS d'étape "Install Parent POM" - ton projet est seul

        stage('Tests - Cours Service') {
            steps {
                // On est déjà à la racine du projet (Cours_Service)
                sh 'mvn clean test'
                echo "✅ Tests du cours-service passés"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=cours-service -Dsonar.projectName="Cours Service" -Dsonar.login=${SONAR_TOKEN}'
                }
                echo "✅ Analyse SonarQube terminée"
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo "✅ Quality Gate passé"
            }
        }
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