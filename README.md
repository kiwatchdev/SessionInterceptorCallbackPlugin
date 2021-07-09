# SessionInterceptorCallbackPlugin

## Dev guide

### Installation
* Clone Openfire
  ```bash
  git clone https://github.com/igniterealtime/Openfire.git
  ```
* Create Openfire run configuration
  * Intellij
    * Run -> Edit Configurations... -> Add Application
    * fill in following values
      * Name: Openfire
      * Use classpath of module: starter
      * Main class: org.jivesoftware.openfire.starter.ServerStarter
      * VM options (not program arguments) (adapt accordingly):
        ```bash
        -DopenfireHome="-absolute path to your project folder-\distribution\target\distribution-base" 
        -Xverify:none
        -server
        -Dlog4j.configurationFile="-absolute path to your project folder-\distribution\target\distribution-base\lib\log4j2.xml"
        -Dopenfire.lib.dir="-absolute path to your project folder-\distribution\target\distribution-base\lib"
        -Dfile.encoding=UTF-8
        ```
      * Working directory: -absolute path to your project folder-
* Clone this project into directory `plugins`

### Use the plugin
* Run/Debug Openfire configuration
* Generate plugin jar `mvn clean package`
* In Openfire admin UI, import plugin assembly jar generated into folder `target`
* Add System property `plugin.session_interceptor.url`
  * Session connections/disconnections are sent to this url
  * Json payload :
    ```json
    {
      "username": "mylogin",
      "date": "2021-07-09T12:39:23.925+02:00[Europe/Paris]", // ISO8601 string
      "event": "CONNECTION|DISCONNECTION"
    }
    ```
