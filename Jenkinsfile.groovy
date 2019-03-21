node {
    properties([parameters([string(defaultValue: '127.0.0.1', description: 'Please give an IP address to build a site ', name: 'IP', trim: true)])])
    
    stage("Install git"){
        sh "ssh ec2-user@${IP}  sudo yum install git python-pip -y"
    }
    stage("git clone"){
        git 'https://github.com/akadyrov86/Flaskex.git'
    }
    stage("Copy files"){
        sh "scp  -r *  ec2-user@${IP}:/tmp/"
    }
    stage("Install requirment"){
         sh "ssh ec2-user@${IP}   sudo pip install -r /tmp/requirements.txt"
        
    }
    stage("Run app"){
        sh "ssh  ec2-user@${IP}   python  /tmp/app.py"
    }
} 