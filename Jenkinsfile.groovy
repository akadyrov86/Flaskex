node {
    properties([parameters([string(defaultValue: '127.0.0.1', description: 'Please give an IP address to build a site ', name: 'IP', trim: true)])])
    
    stage("Install git"){
        sh "ssh ec2-user@${IP}  sudo yum install git python-pip -y"
    }
    stage("git clone"){
        git 'https://github.com/akadyrov86/Flaskex.git'
    }
    stage("Run app"){
        try{
        sh "ssh ec2-user@${IP}  sudo mkdir /flaskex 2> /dev/null"
        }
        catch(exec){
            sh "echo folder exist"
        }
    }
    stage("Copy files"){
        sh "scp  -r *  ec2-user@${IP}:/home/ec2-user"
    }
    stage("Move files to /fleskex"){
        try{
        sh "ssh ec2-user@${IP}  sudo mv /home/ec2-user/*  /flaskex/"
        }
        catch(exec){
            sh "echo Folder moves"
        }
    }
    stage("Install requirment"){
         sh "ssh ec2-user@${IP}   sudo pip install -r /flaskex/requirements.txt"
        
    }
    stage("move service to /etc"){
        sh "ssh ec2-user@${IP} sudo mv /flaskex/flaskex.service  /etc/systemd/system"
    }
    stage("Start service"){
        sh "ssh ec2-user@${IP}  sudo systemctl start flaskex"
    }
} 