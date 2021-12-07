const fs = require("fs");
global.fetch = require("node-fetch");
const base64Img = require("base64-img");
const data = null;
const destpath = "";
const filename = "scr";
const prompt = require("prompt-sync")();
const url = 'http://localhost:8080/remotehost';



let choice = 0;
(async () => {
  try {
    while (choice != 4) {
      console.log("Menu: \n");
      console.log("1-Get properties\n");
      console.log("2-Screenshot\n");
      console.log("3-Reboot remote system\n");
      console.log("4- Exit\n");
      choice = await prompt("\tEnter your choice: ");


      if (choice == 1) {
        // Launch a GET Request to get properties of remote host
        await fetch(url + "/properties")
          .then((response) => {
            return response.json();
          })
          .then((result) => {
            if (result == null) {
              console.log("The properties of the remote host could not be retrieved")
            }
            else {
              console.log("--------------------------------------")
              console.log("Remote Host OS Name: " + result[0])
              console.log("Remote Host OS Version: " + result[1])
              console.log("--------------------------------------")
            }

          })

          .catch((err) => console.log("Request Failed", err)); // Catch potential errors

      }

      else if (choice == 2) {
        // Launch a GET Request to get screenshot of remote host
        await fetch(url + "/screenshot")
          .then((r) => {
            return r.text();
          })
          .then((res) => {
            if (res == null) {
              console.log("Screenshot could not be taken");
            } else {
              function imageSave(filename, res) {
                var myBuffer = Buffer.from(res, "base64");
                fs.writeFile(filename, myBuffer, function (err) {
                  if (err) {
                    console.log(err);
                  } else {
                    console.log("--------------------------------------")
                    console.log("\n\tImage Saved Successfully !");
                    console.log("--------------------------------------")
                  }
                })
              }
              var today = new Date();
              var date = today.getFullYear() + '_' + (today.getMonth() + 1) + '_' + today.getDate();
              var time = today.getHours() + "_" + today.getMinutes() + "_" + today.getSeconds();
              var currentdateTime = date + '_' + time;
              imageSave(currentdateTime + "img.jpg", res);
            }
          })
          .catch((err) => console.log(err));

      }
      else if (choice == 3) {
        // Launch a GET Request to reboot remote host
        await fetch(url + "/reboot")
          .then(response => { return response.text() })
          .then(status => {
            if (status == "true") {
              console.log("Remote Host Rebooted Successfully")
            }
            else (status == "false")
            console.log("Remote Host Could not be Rebooted")

          })

      }

    }
  } catch (error) {
    console.log(error);
  }

})()
