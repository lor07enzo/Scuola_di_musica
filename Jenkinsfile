pipeline {
    agent any

    stages {
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