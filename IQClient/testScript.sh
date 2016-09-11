bladeRF-cli -l hostedx40-latest.rbf
bladeRF-cli -e "set samplerate 1000000"
bladeRF-cli -e "set bandwidth 28000000"
bladeRF-cli -e "set frequency 2465000000"
bladeRF-cli -e "rx config file=dataSets/2016-09-11-02-27-25.bin format=bin n=500000; rx start; rx wait;"
osascript -e 'tell application "Terminal" to quit' &
exit
