# Jacoco and SonarQube (and some extra steps)

A sample Android project to learn how to setup a project with code coverage reports, integrating with SonarQube for continuous quality of code, automating all the steps with Azure Pipelines and Firebase App Distribution of a Debug version (APK) and a Release Version (AAB).

### Jacoco
A free code coverage library for Java. In this project, we use the jacoco library to generate coverage reports of our project. There are two gradle files to generate the jacoco reports, *jacoco/modules.gradle* and *jacoco/project.gradle*.
The *jacoco/modules.gradle* generates coverage reports for all the project modules. The reports are generated in *${project.buildDir}/reports/jacoco/debugCoverage/*. To run the module report generation:
`./gradlew debugCoverage`
The *jacoco/project.gradle.* merges all the modules coverage reports into a final report in *build/reports/jacoco/allDebugCoverage/*. To run the project merge report generation:
`./gradlew allDebugCoverage`

### SonarQube
A platform for continuous inspection of code quality to perform automatic reviews with static analysis. In this project, the SonarQube is setup in the project *build.gradle* file:
```
sonarqube {  
  properties {  
  property "sonar.projectName", "jacocoandsonar"  
  property "sonar.sourceEncoding", "UTF-8"  
  property "sonar.sources", "src/main/java"  
  property "sonar.projectKey", "jacocoandsonar"  
  property "sonar.host.url", "http://localhost:9000"  
  property "sonar.login", "here-it-goes-the-sonar-qube-token"  
  property "sonar.tests", ["src/test/java", "src/test/kotlin"]  
        property 'sonar.coverage.jacoco.xmlReportPaths', "${projectDir}/build/reports/jacoco/allDebugCoverage/allDebugCoverage.xml"  
  }  
}
```
To run the SonarQube Community Edition, download it from https://www.sonarqube.org/success-download-community-edition/. Extract the content, access the downloaded path, */bin/**select-here-the-os-path**/*. Run the sonar qube console (on macOS, for example, run `sh sonar.sh console`). When the message *SonarQube is operational* is shown, we can open the SonarQube in the browser via http://localhost:9000 and log in with  user `admin` and password `admin` (address set in the sonar qube setup, in the project *build.gradle* ). After the login is done, click on "Create a project", set a project name and click on "Set Up " button. In the *How do you want to analyze your repository?* section, select *Locally*, generate a token, **save the token**, and on the *What option best describes your build?* section, select *gradle*. The SonarQube will generate the code to implement the sonar in the project gradle file and it will generate the command to run the sonar qube with some parameters that we can ignore, because in this project, all the configuration is already done in the `sonarQube` code above, placed in the project build file.
Before running the sonar qube command to read our project and upload the information to Sonar Qube Console, we need to use the token we saved earlier and place it on the sonar qube project configuration: `property "sonar.login", "**saved-token-here**"`
Now, run `./gradlew sonarQube`. The project code quality and test coverage information will be uploaded and be available in the Sonar Qube via http://localhost:9000.

**Azure Pipelines** -> Build, test, and distribute. With Azure Pipelines we can automate the process of running our tests, generating our reports, uploading the results to Sonar Qube and distributing the APKs and AABs of our project into the Firebase App Distribution.
In this project, there are two pipeline files: */azure/azure-pipelines-master.yml*, a pipeline that will be triggered only when the `master` branch receives new code and */azure/azure-pipelines-pr.yml* that will be triggered whenever other branches receives new code (current branch rules: develop , feature/*, fix/*, refact/*).
To configure new Pipelines in the Azure DevOps from existent files, go to the Azure DevOps page, *Azure Pipelines*, click on "New Pipeline", select *Other Git*, select *Azure Repos Git*,   select your project, select *Existing Azure Pipelines YAML file*, inform the path *azure/azure-pipelines-pr.yml* and click in *Continue*. Repeat the proccess and inform the */azure/azure-pipelines-master.yml* path.
Now, there are two different Pipelines set in the project on the Azure DevOps.
* azure/azure-pipelines-pr.yml: Builds a debug version, generate coverage report with jacoco for each module, generate the project merged coverage report, publishes the coverage report and APK into the Pipeline Run artifacts (just for the tests sake), execute the sonar qube, and upload the APK to the Firebase App Distribution.
  In the Sonar Qube task, it's being created an environment variable to be used in the project:
```
env:  
  SONAR_LOGIN: $(SONAR_LOGIN)
```
The `env` keyword indicates the creation of variables that can be used in our project.
The `SONAR_LOGIN:` is the new variable we are creating that will be used in our project. In this case, the `SONAR_LOGIN` it's being used in the `sonarQube {}` function declared int the project gradle file.
And the `$(SONAR_LOGIN)` is a variable set inside in the Azure Pipeline, to remove sensitive data from the repository. To add a Azure Pipelines variable, go to the Azure DevOps page, *Azure Pipelines*, select the desired Pipeline, click on *Edit*, click on "Variables", click in *+* (New variable), insert the *Name* and the *Value*. In this project case, the name of the variable is *SONAR_LOGIN* and the value is the token generated when creating the project in the Sonar Qube.

Now, when trying to run the Pipeline from the Azure DevOps, there will be an error when trying to publish the Sonar Qube results, because the Azure Agent won't reach the Sonar Qube service, because we are running the Sonar Qube service locally (http://localhost:9000). To be able to publish the Sonar Qube results in our local service, we need to create a new Azure Agent locally, add it to our Azure DevOps project pool, and force our pipelines to be executed in the pool where our local Azure Agent was added.

Tutorial to create a self-hosted agent and add it to the agent pools: https://docs.microsoft.com/en-us/azure/devops/pipelines/agents/agents?view=azure-devops&tabs=browser.

After setting up the local Azure Agent and adding it to the Azure Agent **Default** Pool, we just need to add in the pipeline files the pool where we want to run the pipeline (this is already set in this project pipeline files):
```
pool:  
  name: Default
```
The last task in our pipeline helps us to distribute our app APK with Firebase Distribution, helping us with our functional tests. Here it is the steps to make it work properly:
* Create an account on Firebase.
* Go to Firebase Console.
* Add a new project.
* Add and Android app.
  *  Add the project package name + ".debug" at the end.
  * Name it with the Project Name + Debug (to make easier to separate the versions)
  * Download the config file and add it inside the project.
  * Add the Android Firebase SDKs.
  * Sync the project.
* Update the gradle file buildTypes configuration to point to the new google-services.json information, so in the `firebaseAppDistribution` section:
  * Update the `appId` with the `mobilesdk_app_id` found in the *google-services.json` file.
  * Update the `groups` to the group name that we will create in the Firebase App Distribution. (To create a Firebase App Distribution Group, go to the Left Menu, *Release & Monitor*, *App Distribution*, *Testers and Groups*, *Add Group*, enter a **group name** and add the google accounts. Use the **group name** recently created to update the `groups` value in the gradle file).
* Additional information: the `applicationIdSuffix` will add a sufix in the package name before sending the APK to the Firebase. This is necessary to differentiate the debug app from the release app, for example. And that's why when creating the Android App in the Firebase, it was added the ".debug" at the end of the package name.

Now, all we need is to create a new branch to push the all the changes to our reporsitory in the Azure Devops (and trigger the *azure-pipelines-pr.yml*).