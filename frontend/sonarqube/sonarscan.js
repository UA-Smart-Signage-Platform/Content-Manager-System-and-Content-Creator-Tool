const scanner = require('sonarqube-scanner');

scanner(
    {
        serverUrl: 'http://sonarqube:9000',
        token: "sqp_b9e9608eca733ff669dd65a210508969dba53feb",
        options: {
            'sonar.projectName': 'react-no-restart',
            'sonar.projectDescription': 'Here I can add a description of my project',
            'sonar.projectKey': 'react-no-restart',
            'sonar.projectVersion': '0.0.1',
            'sonar.exclusions': '',
            'sonar.sources': 'src',
            'sonar.sourceEncoding': 'UTF-8',
        }
    },
    () => process.exit()
)