/* sendJSON()
 * Make basic request to API
 * console out the result
 */
function sendJson(doRecap){
    let request_ob = {
        type: "web"
    }; 
    if(doRecap){
        grecaptcha.enterprise.ready(
            function() {
                grecaptcha.enterprise.execute(site_key, {action: "mobapidev_web"}).then(
                    function(token) {
                        request_ob = {
                            type: "web",
                            recapToken: token.toString()
                        };          
                        let request_json = JSON.stringify(request_ob);        
                        doPost(request_json);                        
                    }
                );
            }
        );        
    }
    else{ 
        let request_json = JSON.stringify(request_ob);
        doPost(request_json);
    }        
}

function doPost(request_json){
    $.ajax(
        {
            type: "POST",
            beforeSend: function(request) {
                request.setRequestHeader("X-API-Key", api_key);
            },
            url: "api",
            data: request_json,
            processData: false,
            success: function(data, status, xhr) {
                console.table(data);
                document.getElementById("output").value=data+document.getElementById("output").value;
            },
            fail: function(jqxhr, settings, ex) { 
                console.table('failed, ' + ex); 
            }
        }
    );
}