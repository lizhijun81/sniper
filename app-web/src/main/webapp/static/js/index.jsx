var Panel = React.createClass({
    render: function () {
        return (
            <div className="panel">
                <div className="header">使用说明</div>
                <div className="inner">
                    <ul>
                        <li className="sidebar-item"><a>我的博客</a></li>
                        <li className="sidebar-item"><a>我的博客</a></li>
                        <li className="sidebar-item"><a>我的博客</a></li>
                    </ul>
                </div>
            </div>
        )
    }
});
var Topic = React.createClass({
    render: function () {
        var topic = this.props.topic;
        return (
            <div className="cell">
                <a className="user-avatar">
                    <img src={topic.userAvatar}/>
                </a>
                <span className="reply_count">
                    <span className="count_of_replies">{topic.countOfReply}</span>
                    <span className="count_separator">/</span>
                    <span className="count_of_visits">{topic.countOfVisit}</span>
                </span>
                <span className="to-top">置顶</span>
                <a>{topic.articleTitle}</a>
                <span className="last-time">
                    <img className="user-avatar-small" src={topic.userAvatar}/>
                        <span className="last-active-time">{topic.lastActiveTime}</span>
                </span>
            </div>
        )
    }
});
var Pagination = React.createClass({
    getInitialState: function () {

        return {
            page: 1,
            size: 15,
            total: 70
        }
    },
    render: function () {
        var total = this.state.total;
        var pageSize = this.state.size;
        var pageNo = this.state.page;
        var totalPage = Math.ceil(total / pageSize);
        var liList = [];
        if (totalPage > 1) {
            //<li><a>«</a></li>
            if (total > pageSize) {
                var cls = "";
                var min = pageNo - 1;
                if (pageNo - 1 == 0) {
                    cls += "disable";
                    min = 1;
                }
                var prev = <Option key={0} class={cls} page={min} size={pageSize} text="«" onclick={this.onclick}/>;
                liList.push(prev);
            }
            for (var i = 0; i < totalPage; i++) {
                var idx = i + 1;
                var li = <Option key={idx} page={idx} size={pageSize} class="" text={idx} onclick={this.onclick}/>;
                liList.push(li);
            }
            //<li><a>»</a></li>
            if (total > pageSize) {
                var cls = "";
                var max = pageNo + 1;
                if (pageNo + 1 > totalPage) {
                    cls += "disable";
                    max = totalPage;
                }
                var next = <Option key={totalPage+1} page={max} size={pageSize} class={cls} text="»"
                                   onclick={this.onclick}/>;
                liList.push(next);
            }
        }
        return (
            <div className="pagination">
                <ul>
                    {liList}
                </ul>
            </div>
        )
    },
    onclick: function (e) {
        var pageNo = e.target.getAttribute('data-page');
        var pageSize = e.target.getAttribute('data-size');
        pageNo = parseInt(pageNo);
        pageSize = parseInt(pageSize);
        var handlePage = this.props.handlePage;
        handlePage(pageNo, pageSize);
    }
});

var Option = React.createClass({
    render: function () {
        var className = this.props.class;
        var text = this.props.text;
        var handle = this.props.onclick;

        var page = this.props.page;
        var size = this.props.size;

        return (
            <li className={className}><a data-page={page} data-size={size} onClick={handle}>{text}</a></li>
        )
    }
});

var TopicList = React.createClass({

    render() {
        var topicList = this.props.data.map(function (topic, idx) {
            return <Topic topic={topic} key={idx}/>
        });
        return (
            <div id="topic_list">
                {topicList}
            </div>
        )
    }
});
var Header = React.createClass({
    render: function () {
        return (
            <div className="nav-header">
                <div className="nav-inner">
                    <div className="container">
                        <a className="my-blog" href="https://github.com/xiaoma20082008" target="_self">我的GitHub</a>
                        <ul className="my-nav">
                            <li><a href="/">首页</a></li>
                            <li><a href="/start">新手入门</a></li>
                            <li><a href="/api">API</a></li>
                            <li><a href="/signup">注册</a></li>
                            <li><a href="/signin">登录</a></li>
                            <li><a href="/about" target="">关于我</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        )
    }
});
var Main = React.createClass({
    getInitialState: function () {
        var topicList = [];
        for (var i = 1; i <= 10; i++) {
            var topic = {
                userAvatar: 'https://avatars.githubusercontent.com/u/156269?v=3&s=120&r=' + i,
                countOfReply: i + 10,
                countOfVisit: i + 100,
                articleTitle: 'title ' + i + ' 不错啊!',
                lastActiveTime: '最后一次:' + i * 60
            };
            topicList.push(topic);
        }
        return {
            data: topicList
        }
    },
    componentDidMount: function () {
        var api = this.props.api;
        $.ajax({
            url: api,
            dataType: 'json',
            data: {
                page: 1,
                size: 15
            },
            success: function (topicList) {
                this.setState({data: topicList});
            }.bind(this),
            error: function (xhr, status, err) {
                console.log(xhr, status, err.toString());
            }.bind(this)
        })
    },
    render: function () {
        return (
            <div>
                <div id="sidebar">
                    <Panel/>
                    <Panel/>
                    <Panel/>
                    <Panel/>
                    <Panel/>
                </div>
                <div id="content">
                    <div className="panel">
                        <div className="header">
                            <a className="topic-tab">全部</a>
                            <a className="topic-tab">精华</a>
                            <a className="topic-tab">分享</a>
                            <a className="topic-tab">问答</a>
                        </div>
                        <div className="inner">
                            <TopicList data={this.state.data}/>
                            <Pagination handlePage={this.handlePage}/>
                        </div>
                    </div>
                </div>
            </div>
        )
    },
    handlePage: function (pageNo, pageSize) {
        console.log("pageNo:" + pageNo + ",pageSize:" + pageSize);

        var newTopicList = [];

        for (var i = pageNo; i <= pageNo - 1 + 10; i++) {
            var topic = {
                userAvatar: 'https://avatars.githubusercontent.com/u/156269?v=3&s=120&r=' + i,
                countOfReply: i + 10,
                countOfVisit: i + 100,
                articleTitle: 'pageNo:' + i + ' 不错啊!',
                lastActiveTime: '最后一次:' + i * 60
            };
            newTopicList.push(topic);
        }

        this.setState({data: newTopicList})
    }
});
var Footer = React.createClass({
    render: function () {
        return (
            <div id="footer-main">
                <div style={{height: 40 +"px",lineHeight: 40 +"px"}}><a>源码地址</a></div>
                <div>
                    <p>暂时不知道写些什么...</p>
                    <p>春晓是个大帅哥!</p>
                    <p>哈哈哈哈</p>
                </div>
            </div>
        )
    }
});

var Container = React.createClass({
    render: function () {
        var api = this.props.url;
        return (
            <div>
                <Header/>
                <Main api={api}/>
                <Footer/>
            </div>
        )
    }
});

ReactDOM.render(
    <Container url="api/topics" optionalArray />,
    document.getElementById('main-body')
);

React.createClass({
    propTypes: {
        // 可以声明 prop 为指定的js基本类型. 默认情况:可传可不传
        optionalArray: React.PropTypes.array,
        optionalBool: React.PropTypes.bool,
        optionalFunc: React.PropTypes.func,
        optionalNumber: React.PropTypes.number,
        optionalString: React.PropTypes.string,
        optionalObject: React.PropTypes.object,

        optionalNode: React.PropTypes.node,

        optionalElement: React.PropTypes.element,

        // 用js的instanceof操作符声明prop为类的实例.
        optionalMessage: React.PropTypes.instanceOf(message),

        // 用enum来限制prop只接受指定的值
        optionalEnum: React.PropTypes.oneOf(['News', 'Photos']),

        custom: function (props, propName, componentName) {

        }

    }
});