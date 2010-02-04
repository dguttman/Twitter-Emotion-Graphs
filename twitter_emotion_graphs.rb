# Emotion Graph
require 'java'
require 'yaml'
require 'pp'

def create_config_yaml
  f = File.new("config.yaml", "w")
  f << "username: your_twitter_username\npassword: your_twitter_password"
  f.close
  raise "Add your twitter account information to config.yaml"
end

if File.exists?("config.yaml")
  CONFIG = YAML.load_file("config.yaml")
  if CONFIG["username"] == "your_twitter_username" || CONFIG["password"] == "your_twitter_password"
    raise "Add your twitter account information to config.yaml"
  end
else
  create_config_yaml
end

class EmotionGraph < Processing::App

  load_libraries :synesketch, :json, :http_stream
  
  import "synesketch"
  import "synesketch.emotion"

  EMOTIONS = %w(happiness sadness anger fear disgust surprise).map {|e| e.to_sym }
  
  EMOTION_COLORS = {:happiness => [240, 220, 10],
                    :sadness => [28, 75, 152],
                    :anger => [200, 80, 18],
                    :fear => [60, 60, 60],
                    :disgust => [109, 80, 27],
                    :surprise => [248, 0, 248],
                    :positive => [10, 200, 30],
                    :negative => [200, 10, 30]}



  def setup
    render_mode JAVA2D
    @frame_rate = 15
    smooth
    background 0

    @tf = TwitterFeel.new
    
    @bar_width = width.to_f / EMOTIONS.size
    @graph_height = (5*height.to_f/6)

    @max_line_height = @graph_height / (EMOTIONS.size+1)
    @frame_n = 1
    @last_emotions = {}
  end
  
  def draw
    frame_rate @frame_rate
    
    @x = @frame_n % width
    
    @frame_n += 1
    
    draw_wipe_line
    
    # draw_strong_emotion

    @tf.process_emotions

    i = 1
    @tf.weighted_emotions.each do |name, weight|
      if name == :valence
        draw_valence(weight, i)
      else
        draw_emotion_graph(name, weight, i)
      end
      i += 1
    end
    
    adjust_frame_rate
    
  end

  def adjust_frame_rate
    @last_wqs_rate ||= @tf.wqs_rate
    if @tf.wqs_rate != @last_wqs_rate
      # p "word queue: #{@tf.word_queue.size} / frame rate: #{@frame_rate} / twitter rate #{@tf.twitter_rate} / wqs_rate #{@tf.wqs_rate}"      
      @frame_rate *= 1.05 if @tf.wqs_rate >= 0.5
      @frame_rate *= 0.95 if @tf.wqs_rate <= 0.0
      @last_wqs_rate = @tf.wqs_rate
    end
    
  end
  
  def draw_valence(weight, i)
    last_x = @x-1
    this_x = @x
    
    min_y = i*@max_line_height-1 
    this_y = min_y - @max_line_height*weight
    last_y = @last_emotions[name] ? @last_emotions[name][1] : min_y
    soft_y = (0.05)*this_y + (0.95)*last_y
    
    hi_y = min_y - @max_line_height
    lo_y = min_y + @max_line_height
    j = (soft_y-lo_y)/(hi_y-lo_y)
    k = 1-j
    
    # r, g, b = soft_y <= min_y ? EMOTION_COLORS[:positive] : EMOTION_COLORS[:negative]
    r, g, b = 100+ -weight*1.5*255, 100 + weight*1.5*255, 80
    sw = 1
    
    stroke(r-30, g-30, b-30, 255)
    line this_x, min_y, this_x, this_y-sw
    
    # r, g, b = r-60, g-60, b-60 if weight < 0
    
    stroke(r-30, g-30, b-30, 255)
    
    line this_x, this_y-sw, this_x, this_y        
    
    @last_emotions[name] = [this_x, this_y]
  end
  
  def draw_strong_emotion
    @last_strong_emotion_frame ||= 0
    strongest_weight = @state.getStrongestEmotion.weight
    emotion = EMOTIONS[@state.getStrongestEmotion.getType]
    if strongest_weight > 0.5 && emotion && @frame_n > @last_strong_emotion_frame + 20
      p @current_words
      r, g, b = EMOTION_COLORS[emotion]

      no_stroke
      fill r, g, b
      # rect_mode(CENTER)
      rect @x, @graph_height + @max_line_height/2, 5, 5
      @last_strong_emotion_frame = @frame_n
    end
  end
  
  def draw_emotion_graph(name, weight, i)
    last_x = @x-1
    this_x = @x
    
    min_y = i*@max_line_height-1 
    this_y = min_y - @max_line_height*weight
    last_y = @last_emotions[name] ? @last_emotions[name][1] : min_y

    soft_y = (0.05)*this_y + (0.95)*last_y
    
    r, g, b = EMOTION_COLORS[name]
    
    stroke(r-30, g-30, b-30, 255)
    line this_x, min_y, this_x, this_y
    
    stroke(r, g, b, 255)
    line last_x, last_y, this_x, this_y        
    
    @last_emotions[name] = [this_x, this_y]


  end
  
  def draw_wipe_line
    no_stroke
    rect_mode(CORNER)

    # fade box for boxes
    fill 0, 20
    rect @x+20, @graph_height, 40, height
    
    # fade box for graphs
    fill 0, 20
    rect @x, 0, 40, height
    
    # solid black line to clear boxes
    stroke_weight 1
    stroke 0, 255
    line @x+20, @graph_height, @x+20, height
    
    # solid black line to clear graphs
    stroke 0, 255
    line @x+1, 0, @x+1, height
    
    # grey line for tracking
    stroke 255, 20
    line @x+2, 0, @x+2, @graph_height
    

  end
  
  def fade(amt)
    fill 0, amt
    rect 0, 0, width, height
  end
  
  def print_emotions(words)

    @state = Empathyscope.getInstance().feel(words)
  
    EMOTIONS.each do |emotion|
      weight = @state.send("#{emotion}_weight")
      p "#{emotion}: #{weight}"
    end
  end
  
  def key_pressed
    exit if key == "q"
  end

end

class TwitterFeel

  import "synesketch"
  import "synesketch.emotion"
  
  EMOTIONS = %w(happiness sadness anger fear disgust surprise).map {|e| e.to_sym }
  ALPHA = 0.05
  
  attr_reader :emotions, :weighted_emotions, :word_queue, :twitter_rate, :wqs_rate
  
  def initialize
    @currentEmotionalState = EmotionalState.new
    @state = EmotionalState.new
    @word_queue = []
    
    @twitter_rate ||= 5
    @last_word_time ||= Time.now.to_f - 0.5
    @last_wqs ||= 0
    @wqs_rate = 0
    @n = 0
    
    @emotions = {}
    @weighted_emotions = {}
    @previous_emotions = {}
    @previous_weighted_emotions = {}
    
    @average_weights = {}
    do_http
  end
  
  def queue_words(words)
    
    calculate_rates
      
    @word_queue << words

  end
  
  def calculate_rates
    @n += 1
    
    if @n == 50
      now = Time.now.to_f
      dtime = now - @last_word_time
      @twitter_rate = @n/dtime
      @wqs_rate = (@word_queue.size - @last_wqs)/dtime
      @last_word_time = now
      @last_wqs = @word_queue.size
      @n = 0
    end
  end
  
  def process_emotions
    get_emotions
    weight_emotions
  end
  
  def weight_emotions
    @emotions.each do |name, weight|
      @weighted_emotions[name] ||= 0
      @previous_emotions[name] = weight
      @previous_weighted_emotions[name] = @weighted_emotions[name]
      @weighted_emotions[name] = (ALPHA * weight) + ( (1-ALPHA) * @weighted_emotions[name] )
    end
  end
  
  def get_emotions
    if @word_queue.size > 0
      words = @word_queue.shift
      @state = Empathyscope.getInstance().feel(words)
      emotions = {}
    
      EMOTIONS.each do |name|
        weight = @state.send("#{name}_weight")
        # emotions[name] = weight
        emotions[name] = weight * 3
                
        @average_weights[name] ||= {:weight => 0, :count => 0}
        @average_weights[name][:weight] = ((@average_weights[name][:weight] * @average_weights[name][:count]) + weight)/(@average_weights[name][:count]+1)
        @average_weights[name][:count] += 1

      end

      special = false

      @average_weights.each do |name, h|
        special = true if emotions[name] > h[:weight]*8
      end

      emotions[:valence] = @state.valence
      @emotions = emotions
      
      if special
        puts "#{words}"
        puts "#{emotions.keys.map {|s| s.to_s + " "*(15-s.to_s.size)}.join("\t")}"
        puts "#{emotions.values.map {|s| s.to_s[0..13] + " "*(15-s.to_s[0..13].size)}.join("\t")}"
        puts "-------------------------------------"
      end
    
    end
  end
  
  def do_http
    Thread.new do
      url = URI.parse("http://#{CONFIG["username"]}:#{CONFIG["password"]}@stream.twitter.com/1/statuses/sample.json")

      HttpStream.get(url, :symbolize_keys => true) do |status|
        status_hash = JSON.parse(status)
        if status_hash["user"]
          words = status_hash["text"]
          lang = status_hash["user"]["lang"]
          queue_words(words) if words && lang == "en"  #time_zone =~ /\(US & Canada\)/
        end
      end
    end
  end
  
end


$app = EmotionGraph.new :title => "Twitter Emotion Graphs", :width => 1280, :height => (768-93)