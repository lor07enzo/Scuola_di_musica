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
                echo 'Creazione del file WAR...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Deploy su Tomcat') {
            steps {
                script {
                    echo 'Pulizia container precedenti...'
                    sh 'docker rm -f tomcat-scuola || true'
                    
                    echo 'Avvio Tomcat sulla porta 8081...'
                    sh 'docker run -d -p 8081:8080 --name tomcat-scuola tomcat:latest'
                    
                    // Qui potresti aggiungere il comando per copiare il WAR dentro il container
                    sh 'docker cp target/*.war tomcat-scuola:/usr/local/tomcat/webapps/'
                }
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