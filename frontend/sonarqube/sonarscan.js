const scanner = require('sonarqube-scanner');

scanner(
    {
        serverUrl: 'http://sonarqube:9000',
        token: "sqp_4a8ac99ccd3dee8c55845ab16577317445c48b46",
        options: {
            'sonar.projectName': 'PI-frontend',
            'sonar.projectDescription': 'Here I can add a description of my project',
            'sonar.projectKey': 'PI-frontend',
            'sonar.projectVersion': '0.0.1',
            'sonar.exclusions': '',
            'sonar.sourceEncoding': 'UTF-8',
        }
    },
    () => process.exit()
)