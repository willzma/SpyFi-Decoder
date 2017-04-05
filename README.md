# SpyFi-Decoder
PennApps 14 project, takes raw bladeRF data, then packages and formats it for data analysis. This is done through a threaded Java application using a simple producer/consumer model to parse bladeRF binaries and then sent them to MongoDB.

#Inspiration
From Wikipedia:

####In cryptography, a side-channel attack is any attack based on information gained from the physical implementation of a cryptosystem, rather than brute force or theoretical weaknesses in the algorithms (compare cryptanalysis). 

As tech products become more prevalent, it is our duty to stay on top of the latest threats to our privacy. Our team stumbled across a potential side-channel attack while playing around with a neat piece of hardware called the bladeRF, a software defined radio.

We discovered that a number of laptops and phones contain network cards that emit a noticeable amount of radio radiation when downloading content from the internet. Apple products in particular displayed the greatest amount of RF activity. We decided to build on this discovery with an interactive IoT data analysis and visualization platform, that we've dubbed SpyFi.

#What it does
Our shielded bladeRF listens on a 2.4GHz frequency band for sustained spikes in activity. When holding certain vulnerable phones or laptops close by and downloading internet content, radiation from the device's network card is detected and sent to our Linode server where the data is processed and analyzed, and eventually displayed via a Google Polymer web app, which depicts meaningful graphs and visualizations of the captured radiation.

#How we built it
###bladeRF
The bladeRF is a software defined radio that can be tuned to pick up a range of frequencies. We were using a 2.4Ghz frequency to measure RF leaks from internet-enabled devices. The blade outputs data in IQ format, which needs further processing and interpretation to make it meaningful.

###Linode
Linode is responsible for hosting our web platform, including our Google Polymer app and our MongoDB backend. We run intensive analytical calculations on Linode to avoid overhead on the bladeRF.

###Mongo
We used a MongoDB backend, hosted on our Linode server. It receives processed bladeRF IQ data chunks which are then picked up by the frontend for data visualization. Mongo's features and flexibility, namely its capped collections and RESTful API, really tied together our software and hardware.

#Challenges we ran into
We had never used a blade before, and getting to know what it was capable of was a dauntingly complicated yet thrilling task.

#Accomplishments that we're proud of
We created a polished hack that works perfectly (we hope). Also, we managed to incorporate a huge number of technologies into one project, from capturing signals with the bladeRF to data visualizations with Google Polymer.

#What we learned
A healthy bit of math and physics, including transforming IQ data and designing a Faraday cage. We also gained experience with Polymer, advanced uses for MongoDB, experience with lower level technology and bit manipulation.

#What's next for SpyFi
We plan to apply more advanced statistical techniques (such as those seen in side-channel power analysis) to extrapolate more impactful data, applying components of differential power analysis to see if we can extract sensitive information.
