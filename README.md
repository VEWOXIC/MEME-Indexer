# MEME-Indexer 📷🔍

A CLIP-based meme indexer app that can manage and search the memes on your phone with natural language! 

**Never be without the perfect meme again!** 😎

![](./imgs/have_a_meme.jpg)
> Wait, I have a meme for this! 🤣

(Also applicatable to any images, not just memes 🤫)

## NEW SERVER ADDRESS!!!

The original CLIP backend server stops for service. The new server address is as below: 

```bash
http://202.79.96.144:50547/api/process
```

Please go to settings->server address to change it! Note that v0.2.0 does not support plaintext http connection, please upgrade to v0.2.1. 

## Feature ✨

- Support searching your image using both Chinese and English 🌍
- Directly send it to your chat app! WeChat or WhatsApp! 🚀
- We provide a backend server with CN-CLIP-Large model, you can directly use it to get image/text embedding for image index and query 🔧
- You can set up your own CLIP back end server! You can find backend code [here](https://github.com/xywen97/cn_clip_server). Of course, we encourage everyone to share their server 🤝
- Efficient enough. With 2k images indexed, MEME Indexer only takes about 30MB stoarge 🚀

## Installation 📲

Download APK file [here](https://github.com/VEWOXIC/MEME-Indexer/releases). 

This is a pre-release version, you may need to agree to install the app from an unidentified developer 🙈

## Quick Start 🚀

Video demo [在相册里翻找梗图× 直接用语言检索梗图√](https://www.bilibili.com/video/BV1y14y1Z7DT/?share_source=copy_web&vd_source=b327831050842b9cae04db313047af6a) !

- Permit the storage access 📂
<div align="center">
<img src=./imgs/File_access.png width=30% />
</div>

- Go to the "**Add Folder**" page, press the "**+**" button on the botton right to select the folder with images you want to index 📁

<div align="center">
<img src=./imgs/add_folder.gif width=30% />
</div>

- Go to "**Gallary**" page and you will find the images are loaded 📸
- Press start button to start create index for images 🏃‍♂️

<div align="center">
<img src=./imgs/Create_index.gif width=30% />
</div>

- You can filter to check the indexed images 🔎

<div align="center">
<img src=./imgs/Filter.gif width=30% />
</div>

- Go to "**Search**" page and describe your image with natural language, Chinese/English both ok 🤓
- Press "**Search**" to get the query embedding. It will sort your gallary according to semantic correlation 🔍
- Press any image to call the share function 📤

<div align="center">
<img src=./imgs/search.gif width=30% />
</div>

- Send it to WeChat, WhatsApp! 🚀

## Settings ⚙️

- **Server Address**: You can go to the setting menu to change your query url to your own server! 
- **Language**: TODO (I believe you can read the English)
- **Clear Index**: TODO (Not working yet OTZ)

## Future plan 🤔

I am still a full-time PhD student, the maintainance may not be in time. I finish this app in 1 week with the help of ChatGPT. 

- [ ] iOS app is still in development. 
- [ ] Batchity the index building process. 
- [ ] Make it look better. 
- [ ] Maybe personalize the searching result on your device. 


## Privacy Concerns 🔒

We will not keep any of your information on our servers. Your images are compressed into 224\*224 thumbnail on the phone for faster transfer and make it impossible to gather any personal data from them. 

We still recommand not to upload your personal photos (you can anyway). 

Furthermore, you can always set up your own server! 

**We do not guarantee other servers do not gather your information. **

## Acknowledgments

- ChatGPT
- @xywen97

## PS, ChatGPT is awesome! 🤩 He add all the emojis in this README.md! 🤣
