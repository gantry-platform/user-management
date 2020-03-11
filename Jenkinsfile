def source_url = "https://gitlab.mgmt.dev.gantry.ai/gantry/platform/user-management.git"
def harbor = "harbor.dev.gantry.ai"
def harbor_project = "gantry"


podTemplate(
    containers:
    [
        containerTemplate(name: 'maven', image: 'maven:3.6-jdk-11-slim', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true)
    ],
    volumes:
    [
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
        emptyDirVolume(mountPath: '/mnt/mount_pypipe', memory: false)

    ]
)
{
    node(POD_LABEL)
    {
        stage('checkout-source-code') {
             checkout([
                $class: 'GitSCM',
                branches: [[name: 'refs/heads/dev']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [
                    [$class: 'CloneOption', timeout: 300],
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "./source_dir"]
                ],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'GitLab_Chanho', url: source_url]]

            ])
            sh 'ls -la source_dir'
            sh 'pwd'
        }
        stage('checkout-build-code') {
            checkout([
                $class: 'GitSCM',
                branches: [[name: 'refs/heads/dev']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [
                    [$class: 'CloneOption', timeout: 300],
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "./build_dir"]
                ],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'GitLab_Chanho', url: source_url]]

            ])
            sh 'ls -la'
            sh 'pwd'
        }

        stage('build code') {
            container('maven') {
                sh 'cp build_dir/Dockerfile source_dir/.'
                dir("source_dir") {
                    sh 'mvn package -DskipTests'
                }
            }
        }
        stage('check jar file') {
            dir("source_dir") {
                sh 'echo "CHECKING BINARY FILES after BUILD"'
                sh 'ls -la target'
            }
        }
        stage('create image') {
            container('docker') {
                dir("source_dir") {
                    sh 'docker build -t gantry/user-management:0.1 --build-arg JAR_FILE=target/*.jar --build-arg SPRING_PROFILES_ACTIVE=dev .'
                    sh 'docker images'
                }
            }
        }
        stage('push docker image') {
            container('docker') {
                dir("source_dir"){
                   docker.withRegistry("https://${harbor}", "Harbor_Chanho") {
                       sh "docker tag gantry/user-management:0.1 ${harbor}/${harbor_project}/user-management:0.1"
                       sh "docker push ${harbor}/${harbor_project}/user-management:0.1"
                   }
                }
            }
        }

    }
}
