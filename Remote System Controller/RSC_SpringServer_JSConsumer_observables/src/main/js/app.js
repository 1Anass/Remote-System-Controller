var RxHR = require("@akanass/rx-http-request").RxHR;
var Rx = require("rxjs");
var Op = require("rxjs/operators");


var subscriptionObj = null;
afterClick = function () {
    
    try {
        
        if (document.getElementById("button").value == "Start") {
            document.getElementById("button").value = "Stop";
            const period = document.getElementById("period").value;
            observable = RxHR.get("http://localhost:8080/remotehost/screenshot"); //get http response
            subscriptionObj = Rx.interval(period * 1000).subscribe(
                () => {
                    if(subscriptionObj == null)
                        alert("Subscription is null here")
                    observable
                        .pipe(Op.filter((data) => data.response.statusCode === 200))//accept successful responses only
                        .subscribe(
                            (data) => {
                                document.getElementById("RemoteScreen").src = "data:image/png;base64," + data.body;
                                if(subscriptionObj == null)
                                    alert("Subscription is null middle")
                            },
                            (error) => {
                                alert(error);
                            }
                        );
                },
                (error) => {
                    alert(error)
                }
            );
        }
        else {
            if(subscriptionObj == null)
                        alert("Subscription is null end")
            document.getElementById("button").value = "Start";
            subscriptionObj.unsubscribe();
           
        }
    }
    catch (err) {
        alert(err);
    
    }
    
    

};
