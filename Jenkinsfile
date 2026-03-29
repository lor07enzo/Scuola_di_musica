pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/lor07enzo/Scuola_di_musica.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Compilazione del progetto in corso...'
                // Qui aggiungerai i comandi (es: sh './mvnw clean compile' o 'npm install')
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                echo 'Esecuzione dei test unitari...'
                // Esempio: sh './mvnw test'
                sh 'mvn test'
            }
        }
        stage('Package') {
            steps {
                echo 'Creazione del file JAR...'
                // Crea il file .jar eseguibile nella cartella target/
                // Saltiamo i test qui perché li abbiamo appena fatti sopra
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