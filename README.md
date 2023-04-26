# MEME-Indexer

A CLIP-based meme indexer app that can manage and search your meme repository with natural language! 

**Never be without the perfect meme again!**

![](./imgs/have_a_meme.jpg)
> Wait, I have a meme for this!

## Features
- Supports searching your image using both Chinese and English. 
- Provides a backend server with CN-CLIP-Large model, which can be used to get image/text embedding for image index and query. 
- You can set up your own CLIP back-end server! You can find the backend code [here](https://github.com/xywen97/cn_clip_server). Of course, we encourage everyone to share their server. 
- Efficient enough. With 2k images indexed, MEME Indexer only takes about 30MB storage. 

## Installation

Download the APK file [here](https://github.com/VEWOXIC/MEME-Indexer/releases). 

This is a pre-release version; you may need to agree to install the app from an unidentified developer.

## Quick Start

- Permit storage access. 
<div align="center">
<img src=./imgs/File_access.png width=30% />
</div>

- Go to the "**Add Folder**" page, press the "**+**" button on the bottom right to select the folder with images you want to index. 

<div align="center">
<img src=./imgs/add_folder.gif width=30% />
</div>

- Go to the "**Gallery**" page, and you will find the images are loaded. 
- Press the start button to create the index for the images. 

<div align="center">
<img src=./imgs/Create_index.gif width=30% />
</div>

- You can filter to check the indexed images. 

<div align="center">
<img src=./imgs/Filter.gif width=30% />
</div>

- Go to the "**Search**" page and describe your image with natural language, Chinese/English both work. 
- Press "**Search**" to get the query embedding. It will sort your gallery according to semantic correlation. 
- Press any image to call the share function. 

<div align="center">
<img src=./imgs/search.gif width=30% />
</div>

- Send it! 

## Settings

- **Server Address**: You can go to the setting menu to change your query URL to your server! 
- **Language**: TODO (I believe you can read English)
- **Clear Index**: TODO (Not working yet OTZ)

## Future plans

I am still a full-time PhD student, and maintenance may not be timely. I finished this app in one week with the help of ChatGPT. 

- [ ] Batchify the index-building process. 
- [ ] Make it look better. 
- [ ] Maybe personalize the searching result on your device. 


## Privacy Concerns

We will not keep any of your information on our servers. Your images are compressed into 224x224 thumbnails on the phone for faster transfer and make it impossible to gather any personal data from them. 

We still recommend not uploading your personal photos (you can anyway). 

Furthermore, you can always set up your server! 

**We do not guarantee other servers do not gather your information. **

## Acknowledgments

- ChatGPT
- @xywen97
