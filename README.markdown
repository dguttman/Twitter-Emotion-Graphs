Twitter Emotion Graphs
======================

Twitter Emotion Graphs shows you the emotional state of Twitter in real-time.

TEGs uses the [Twitter Streaming API][1], [Synesketch's EmpathyScope][2], and [Ruby-Processing][3] to get tweets streaming in real-time, compute emotion values, and display them in a visually dynamic fashion.

Usage
-----

Before using TEGs, install **ruby-processing**:

    $ sudo gem install ruby-processing

Next, in the twitter_emotion_graphs directory create a **config.yaml** file. You can copy from the included config.yaml.sample, but replace the relevant text with your twitter credentials. It should look something like the following:

    # /twitter_emotion_graphs/config.yaml
    username: dguttman
    password: snowflake

Lastly, run TEGs using ruby-processing's "rp5 run" command:

    $ rp5 run twitter_emotion_graphs.rb
    
Special Thanks
--------------

* Ben Fry and Casey Reas - [Processing][5]
* Jeremy Ashkenas - [Ruby-Processing][3]
* Uroš Krčadinac - [Synesketch][2]
* Luke Redpath and Brian Lopez - [yajl-ruby][4]

  
[1]:http://apiwiki.twitter.com/Streaming-API-Documentation
[2]:http://www.synesketch.krcadinac.com/
[3]:http://wiki.github.com/jashkenas/ruby-processing/
[4]:http://github.com/brianmario/yajl-ruby
[5]:http://processing.org