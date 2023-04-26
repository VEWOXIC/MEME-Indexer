# MEME-Indexer

A CLIP-based meme indexer app that can manage and search your meme repository with natural language! 

Make you never 

![](./imgs/have_a_meme.jpg)
> Wait, I have a meme for this! 

## Feature
- Support searching your image using both Chinese and English. 
- We provide a backend server with CN-CLIP-Large model, you can directly use it to get image/text embedding for image index and query. 
- You can set up your own CLIP back end server! You can find backend code [here](https://github.com/xywen97/cn_clip_server). Of course, we encourage everyone to share their server. 
- Efficient enough. With 2k images indexed, MEME Indexer only takes about 30MB stoarge. 

## Installation

Download APK file [here](https://github.com/VEWOXIC/MEME-Indexer/releases). 

This is a pre-release version, you may need to agree to install the app from an unidentified developer.

## Quick Start

- Permit the storage access. 
- Go to the "**Add Folder**" page, press the "**+**" button on the botton right to select the folder with images you want to index. 
- Go to "**Gallary**" page and you will find the images are loaded. 
- Press start button to start create index for images. 
- You can filter to check the indexed images. 
- Go to "**Search**" page and describe your image with natural language, Chinese/English both ok. 
- Press "**Search**" to get the query embedding. It will sort your gallary according to semantic correlation. 
- Press any image to call the share function. 
- Send it! 

## Settings

- **Server Address**: You can go to the setting menu to change your query url to your own server! 
- **Language**: TODO (I believe you can read the English)
- **Clear Index**: TODO (Not working yet OTZ)

## Future plan

I am still a full-time PhD student, the maintainance may not be in time. I finish this app in 1 week with the help of ChatGPT. 

- [ ] Batchity the index building process. 
- [ ] Make it look better. 
- [ ] Maybe personalize the searching result on your device. 


## Privacy Concerns

We will not keep any of your information on our servers. Your images are compressed into 224\*224 thumbnail on the phone for faster transfer and make it impossible to gather any personal data from them. 

We still recommand not to upload your personal photos (you can anyway). 

Furthermore, you can always set up your own server! 

**We do not gurantee other servers do not gather your information. **

## Acknowledgement

- ChatGPT






