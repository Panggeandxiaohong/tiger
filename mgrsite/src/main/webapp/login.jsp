<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtm1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>微信考试系统</title>
  <link href="/css/login.css" rel="stylesheet" type="text/css" />
  <script src="/js/plugin/jquery-easyui/jquery.min.js"></script>
  <script type="text/javascript" >
      $(document).keyup(function(data){
          if(data.keyCode==13){
              submitForm();
          }
      });

      function submitForm(){
          $.post("/login.do",$("form").serialize(), function (data) {
              console.log(1);
              if (data.success){
                  //跳转到首页
                  window.location.href="/index.do"
              }else {
                  //返回登录界面
                  alert(data.msg);
              }
          })
      }

  </script>
</head>


<body>
<div class="main-login" >
  <div class="login-logo"></div>
  <div class="login-content">

    <form  method="post" >
      <div class="login-info">
        <span class="user">&nbsp;</span>
        <input name="username" id="userName" type="text" value="" class="login-input" required oninvalid="setCustomValidity('账户不能为空！');" oninput="setCustomValidity('');"/>
      </div>
      <div class="login-info">
        <span class="pwd">&nbsp;</span>
        <input name="password" id="password" type="password" value="" class="login-input" required oninvalid="setCustomValidity('密码不能为空！');" oninput="setCustomValidity('');"/>
      </div>
      <div class="login-oper">
        <input  type="checkbox" value=""/>记住密码
      </div>
      <div class="login-oper">
        <input  type="button" value="登 录" class="login-btn"onclick="submitForm()"/>
        <input name="按钮" type="button" class="login-reset" onclick="" value="注 册"/>
      </div>
    </form>
  </div>
  <div class="bottom">版权所有，侵权收费</div>
</div>
<script type="text/javascript">
    window.onload = function(){
        var config = {
            vx: 4,
            vy:  4,
            height: 2,
            width: 4,
            count: 100,
            color: "121, 162, 185",
            stroke: "100,200,180",
            dist: 6000,
            e_dist: 20000,
            max_conn: 10
        }
        CanvasParticle(config);
    }
</script>
<script type="text/javascript" src="/js/views/canvas-particle.js"></script>
</body>
</html>
