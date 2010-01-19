Twitter Emotion Graphs
======================

Twitter Emotion Graphs shows you the emotional state of Twitter in real-time.

TEGs uses the [Twitter Streaming API][1], [Synesketch's EmpathyScope][2], and [Ruby-Processing][3] to get tweets streaming in real-time, compute emotion values, and display them in a visually dynamic fashion.

Usage
-----

Before using TEGs, install **ruby-processing**:

    $ sudo gem install ruby-processing

Next, in the twitter_emotion_graphs directory create a config.yaml file. You can copy from the included config.yaml.sample, but replace the relevant text with your twitter credentials. It should look something like the following:

    username: dguttman
    password: snowflake

Lastly, run TEGs using ruby-processing's "rp5 run" command:

    $ rp5 run twitter_emotion_graphs.rb

  
[1]:http://apiwiki.twitter.com/Streaming-API-Documentation
[2]:http://www.synesketch.krcadinac.com/
[3]:http://wiki.github.com/jashkenas/ruby-processing/