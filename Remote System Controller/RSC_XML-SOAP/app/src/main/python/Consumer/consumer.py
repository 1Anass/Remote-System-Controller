import zeep
import PIL.Image as Image
import io
from datetime import datetime


consumer = zeep.Client(wsdl="../../resources/RemoteControlService.wsdl")
option = 0

while(option!=4):
    print("Available Services:")
    print("1 - Get Name and Version of the operating system of the remote host")
    print("2 - Take a screenshot of the remote system")
    print("3 - Reboot the remote system")
    print("4 - Exit")
    option = int(input("\tchoice: "))
    if option==1:
        Properties = consumer.service.getProperties()
        if Properties is None:
            print("\n-----------------------------------------------------------\n")
            print("\n\t Properties of the remote OS could not be retrieved")
            print("\n-----------------------------------------------------------\n")
        else:
            print("\n-----------------------------------------------------------\n")
            print("\nName: "+Properties[0]+" Version: "+Properties[1])
            print("\n-----------------------------------------------------------\n")
    elif option==2:
        imageBytes = bytearray(consumer.service.screenshot())
        if imageBytes is None:
            print("\n-----------------------------------------------------------\n")
            print("\n\tScreenshot could not be taken, please try again")
            print("\n-----------------------------------------------------------\n")
        else:
            now = datetime.now()
            date_time = datetime.now()
            filename = date_time.strftime("%Y_%m_%d_%H_%M_%S")
            image = Image.open(io.BytesIO(imageBytes))
            image.save(r'Screenshots/'+filename+'.png')
            print("\n-----------------------------------------------------------\n")
            print("\n\tScreenshot Taken Successfully!")
            print("\n-----------------------------------------------------------\n")
    elif option==3:
        isRebooted = consumer.service.reboot()
        if(isRebooted==True):
            print("\n-----------------------------------------------------------\n")
            print("\n\tRemote system rebooted successfully")
            print("\n-----------------------------------------------------------\n")
        else:
            print("\n-----------------------------------------------------------\n")
            print("\n\tRemote system could not be rebooted, please try later.")
            print("\n-----------------------------------------------------------\n")
    elif option==4:
        break
    else:
        continue



