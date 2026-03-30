pipeline {
    agent any

    tools {
        maven 'M3'
    }

    stages {
        // Nota: lo stage 'Checkout' è tecnicamente opzionale nelle Declarative Pipeline
        // perché Jenkins lo fa in automatico all'inizio, ma tenerlo non fa male.
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilazione del progetto in corso...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Esecuzione dei test unitari...'
                // sh 'mvn test'
                sh 'mvn verify'
            }
            post {
                always {
                    // Genera il grafico dei test nell'interfaccia di Jenkins
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Creazione del file JAR...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            // Pulisce la cartella di lavoro per non intasare Docker Desktop
            cleanWs()
        }
        success {
            echo 'Complimenti! La build della Scuola di Musica è terminata con successo.'
        }
        failure {
            echo 'Ops! Qualcosa è andato storto nella build o nei test.'
        }
    }
}