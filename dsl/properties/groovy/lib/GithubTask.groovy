import com.cloudbees.flowpdf.*
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
/**
* Samplepoc
*/
class Samplepoc extends FlowPlugin {

    @Override
    Map<String, Object> pluginInfo() {
        return [
                pluginName     : '@PLUGIN_KEY@',
                pluginVersion  : '@PLUGIN_VERSION@',
                configFields   : ['config'],
                configLocations: ['ec_plugin_cfgs'],
                defaultConfigValues: [:]
        ]
    }
/** This is a special method for checking connection during configuration creation
    */
    def checkConnection(StepParameters p, StepResult sr) {
        // Use this pre-defined method to check connection parameters
        try {
            // Put some checks here
            def config = context.configValues
            log.info(config)
            // Getting parameters:
            // log.info config.asMap.get('config')
            // log.info config.asMap.get('desc')
            // log.info config.asMap.get('endpoint')
            // log.info config.asMap.get('credential')
            
            // assert config.getRequiredCredential("credential").secretValue == "secret"
        }  catch (Throwable e) {
            // Set this property to show the error in the UI
            sr.setOutcomeProperty("/myJob/configError", e.message + System.lineSeparator() + "Please change the code of checkConnection method to incorporate your own connection checking logic")
            sr.apply()
            throw e
        }
    }
// === check connection ends ===
/**
    * sampleProcedure - Sample Procedure/Sample Procedure
    * Add your code into this method and it will be called when the step runs
    * @param config (required: true)
    * @param applicationPath (required: true)
    
    */
    def sampleProcedure(StepParameters p, StepResult sr) {
       // Use this parameters wrapper for convenient access to your parameters
        SampleProcedureParameters sp = SampleProcedureParameters.initParameters(p)

        // Calling logger:
        /*
        log.info p.asMap.get('config')
        log.info p.asMap.get('branchName')
        log.info p.asMap.get("endpoint")
        log.info p.asMap.get("accesstoken")
        log.info p.asMap.get("ownername")
        log.info p.asMap.get("reponame")
        */
        def pat = p.asMap.get("accesstoken")
        def owner = p.asMap.get("ownername")
        def repo = p.asMap.get("reponame")
        def headBranch = p.asMap.get('branchName')

        def baseURL = p.asMap.get("endpoint")
        def conn = null
        def responseText = ""
        try {
            conn = new URL("$baseURL/repos/$owner/$repo/pulls").openConnection();
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "token $pat")
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
            conn.setDoOutput(true)
            conn.getOutputStream().write('{"head":"feature","base":"main","title":"1st pull request"}'.getBytes("UTF-8"));
            def postRC = conn.getResponseCode();
            responseText = conn.getInputStream().getText()
            log.info "Got: $postRC \n $responseText"
        } catch (Exception e) {
            log.info("Exception: ${e.message}")
        } finally {
            conn.disconnect()
        }
        // Setting job step summary to the config name
        sr.setJobStepSummary(p.getParameter('config')?.getValue() ?: 'null')

        sr.setReportUrl("Sample Report", 'https://cloudbees.com')
        sr.apply()
        log.info("step Sample Procedure has been finished")
    }

// === step ends ===

 def orgCreationProcedure(StepParameters p, StepResult sr) {
       // Use this parameters wrapper for convenient access to your parameters
        SampleProcedureParameters sp = SampleProcedureParameters.initParameters(p)

        // Calling logger:
        /*
        log.info p.asMap.get('config')
        log.info p.asMap.get('branchName')
        log.info p.asMap.get("endpoint")
        log.info p.asMap.get("accesstoken")
        log.info p.asMap.get("ownername")
        log.info p.asMap.get("reponame")
        */
        def pat = p.asMap.get("accesstoken")
        def orgname = p.asMap.get("orgName")
        
        def baseURL = p.asMap.get("endpoint")
        def conn = null
        def responseText = ""
        try {
            conn = new URL("$baseURL/orgs/rpandiyarajan24org/repos").openConnection();
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "token $pat")
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
            conn.setDoOutput(true)
            conn.getOutputStream().write('{"name":"feature"}'.getBytes("UTF-8"));
            def postRC = conn.getResponseCode();
            responseText = conn.getInputStream().getText()
            log.info "Got: $postRC \n $responseText"
        } catch (Exception e) {
            log.info("Exception: ${e.message}")
        } finally {
            conn.disconnect()
        }
        // Setting job step summary to the config name
        sr.setJobStepSummary(p.getParameter('config')?.getValue() ?: 'null')

        sr.setReportUrl("Sample Report", 'https://cloudbees.com')
        sr.apply()
        log.info("step Sample Procedure has been finished")
    }

}