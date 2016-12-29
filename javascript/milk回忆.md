# 关于milk

 javascript

---
##简介

 

*[milk][1]这个项目是一个微商城项目,当时公司痴迷于微信公众号商城系统,而我一直也在摸索web app与native
app之间的异同.也是当时集中攻克H5以及javascript的开始,这个开始不是说第一次接触javascript,javascript早已熟用于每个项目之中,而这个开始是第一次以自己对javascript的认知主导开发的项目.*

*在长期的工作中,接触到很多写javascript代码的机会,认为javascript是一款非常有魅力的脚本语言,它不像java一样需要遵循很多规范,可以没有严格的格式标准,可以让开发人员根据自己对代码的"艺术"认知去编写代码.正是基于这点,我决定以自己的风格来一次全面javascript开发作业*

##人员简介

> 我:5年java dev,当时担任该项目主导角色 

> 小陈:刚毕业的应届毕业生,学习前端,特点是有很好的执行力以及理解能力

> 小王:刚毕业的应届毕业生,学习前端,特点是有自己的想法,偏固执


##正文

对于javascript,从我第一次接触的时候我就觉得很有意思,映象比较深的是用jQuery模拟一个交互型网上书城.就是不刷新的话看上去就像一个存在server端的web书城.

后来在和一些前端大神共事过后(当时正值mvvm类js框架兴起时期),发现原来javascript还能编写的如此工整规范,正好符合自己长期从事java所受到的oop思维方式的结构,不再像以前那种单纯的脚本式的代码(当然这不是贬低脚本代码,反而支持oo的脚本代码是非常强悍的,比如python这类).也就是发现了javascript的弱类型能够很好的根据开发者的思想产生出意想不到的变化,而这种变化对oo的理解以及整个项目都是由极大的积极作用.
比如以前常见的js代码:

    function login(username,password){
        if(isVerify([username,password])){
            submit(username,password);
        }else{
            error('There is something is wrong...')
        }
    }
    
    function isVerify(args){
        //some code...
    }
    
    function submit(username,password){
        //some code...
    }
    
    function error(msg){
        //some code...
    }
    
这类代码在以前不会有什么问题,但是现在再回看回去就会发现,这类代码首先它是全局的,其次非常的零散,如果多个页面有类似的行为,可能就会出现大量重复的代码.

而现在会怎么做:

    function User(){
        var instance = {};
        
        function isVerify(args){
             //some code...
        }
        
        function submit(username,password){
            //some code...
        }
        
        instance.login = function(username,password){
            if(isVerify([username,password])){
                submit(username,password);
            }else{
                error('There is something is wrong...')
            }
        } 
        
        return instance;
    }
    
这样就能初步体现出oo的思维方式,开闭原则,你需要login这部分业务逻辑,只需要关注login这个行为,其它的你不需要关心甚至不需要知道.同时其他页面出现类似的行为直接new User()就能解决一些重复代码的问题了,虽然在某些人眼里这和上面的方式没有什么不同,甚至会有人认为你是多此一举,然而我认为这是思想上的进步,是你对软件开发上认知的进步.
所以我希望我能够将自己理解的传授给刚毕业的小朋友们.

基于以上目的,在我当时只掌握jQuery的情况下,果断的决定采用这种"新"的方式来做前端代码.在这个过程中,又了解到了大量的块级思想以及对AMD的认识,于是也使用了requirejs来做模块之间的整合(貌似当时写的代码都还不是标准的requirejs的编码方式),同时所有人也对npm,gulp,bower,nodejs,sass等这些前端新事物有了一个初步认识.

这个项目我认为是成功的,收获颇多.首先两个刚毕业的毕业生给我的反馈是学到了很多在学校里学不到的东西,主要是思维方式,以及工作方式,不会是那种不动脑就开始做的码农,我也很高兴他们会在设计一个对象的时候去思考,去讨论;其次整个开发团队在执行力上有了很好的提升,因为不止是他们,包括我来说都是一种新的尝试,未知的尝试会碰见很多问题,但我们都能一一解决,最后在deadline之前完工(也许我们遇到的问题都是些小儿科问题);再次我将这看做是一个承上启下的过程,集中式的大量前端开发,可以巩固很多前端知识,同时也为后面直接使用react做好铺垫.

为什么说会是为react做好铺垫呢,因为我们当时用的还是jQuery,那么涉及到的dom操作就会出现较多的用js拼接html的代码,我们会把这类代码封装成一个私有的方法,这个方法不会做任何其他事情只会做跟dom有关的处理.这种方式和react的render很类似,对于刚毕业的他们来说在第一次面对react时也不会出现一头雾水的情况.所以说这个项目对我们后面用react高效开发起到了非常重要的承上启下的作用.

最后我想说的是在深入了解了javascript后,我会说javascript是一门非常有魅力的语言.甚至在做了大量关于javascript的开发后再做java会发现java实在是太死板了,太多条条框框了,这些条条框框甚至会严重影响开发效率.假如都能像javascript那样,那是一件多么美好的事情(nodejs已经能够完成类似的工作了)

感觉自己的叙述能力简直就是幼儿级的......


  [1]: https://github.com/wjse/milk
