const scanner = require('sonarqube-scanner');
const { env } = require('node:process');

scanner(
    {
        serverUrl: 'http://sonarqube:9000',
        token: env.TOKEN,
        options: {
            'sonar.projectName': env.PROJECT,
            'sonar.projectDescription': 'Here I can add a description of my project',
            'sonar.projectKey': env.PROJECT,
            'sonar.projectVersion': '0.0.1',
            'sonar.exclusions': '',
            'sonar.sources': 'src',
            'sonar.sourceEncoding': 'UTF-8',
        }
    },
    () => process.exit()
)