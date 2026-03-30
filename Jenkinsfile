pipeline {
    agent any

    tools {
        maven 'M3'
        // Se hai configurato anche un JDK nei Tools (es. 'Java17'), aggiungilo qui:
        // jdk 'Java17' 
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
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Creazione del file JAR...'
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        success {
            echo 'Complimenti! La build della Scuola di Musica è terminata con successo.'
        }
        failure {
            echo 'Ops! Qualcosa è andato storto nella build o nei test.'
        }
    }
}