webpackJsonp([9], {
  P7ry: function (e, r, s) {
    "use strict";
    Object.defineProperty(r, "__esModule", {value: !0});
    var t = s("mvHQ"), a = s.n(t), i = s("1pQF"), n = {
      name: "Login", data: function () {
        return {
          username: "",
          email: "",
          password: "",
          nusername: "",
          nemail: "",
          npassword: "",
          npassword2: "",
          login: 0,
          emailErr: !1,
          passwordErr: !1,
          loginErr: !1,
          loginTitle: "用户名或密码错误",
          nusernameErr: !1,
          nemailErr: !1,
          npasswordErr: !1,
          npassword2Err: !1,
          registerErr: !1,
          registerTitle: "该邮箱已注册",
          err2005: !1,
          step: 1,
          fullscreenLoading: !1,
          urlstate: 0
        }
      }, methods: {
        routeChange: function () {
          this.login = void 0 == this.$route.query.login ? 1 : parseInt(this.$route.query.login), this.urlstate = void 0 == this.$route.query.urlstate ? 0 : this.$route.query.urlstate, 0 == this.urlstate ? (this.err2005 = !1, this.step = 1) : "urlInvalid" == this.urlstate ? (this.err2005 = !0, this.step = 2) : "urlErr" == this.urlstate && (this.err2005 = !0, this.step = 1)
        }, loginEnterFun: function (e) {
          13 == (window.event ? e.keyCode : e.which) && this.gotoHome()
        }, gotoHome: function () {
          var e = this;
          /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(e.email) ? e.emailErr = !1 : e.emailErr = !0, e.password && /^(\w){6,12}$/.test(e.password) ? e.passwordErr = !1 : e.passwordErr = !0, e.emailErr || e.passwordErr || Object(i.m)(e.email, e.password, function (r) {
            1010 == r.code ? (localStorage.setItem("userInfo", a()(r.data)), localStorage.setItem("accessToken", r.token), localStorage.getItem("logUrl") ? e.$router.push({path: localStorage.getItem("logUrl")}) : e.$router.push({path: "/"})) : 2008 == r.code || 2007 == r.code ? (e.loginErr = !0, e.loginTitle = "邮箱或密码错误") : 2009 == r.code ? (e.loginErr = !0, e.loginTitle = "该邮箱注册码未激活，请前往邮箱激活") : 2005 == r.code ? e.err2005 = !0 : (e.loginErr = !0, e.loginTitle = "登录失败")
          })
        }, registerEnterFun: function (e) {
          13 == (window.event ? e.keyCode : e.which) && this.newRegister()
        }, newRegister: function () {
          var e = this;
          e.nusername ? e.nusernameErr = !1 : e.nusernameErr = !0, /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(e.nemail) ? e.nemailErr = !1 : e.nemailErr = !0, e.npassword && /^(\w){6,12}$/.test(e.npassword) ? (e.npasswordErr = !1, e.npassword == e.npassword2 ? e.npassword2Err = !1 : e.npassword2Err = !0) : e.npasswordErr = !0, e.nusernameErr || e.nemailErr || e.npasswordErr || (e.fullscreenLoading = !0, Object(i.r)(e.nusername, e.npassword, e.nemail, function (r) {
            if (1010 == r.code) var s = setTimeout(function () {
              e.fullscreenLoading = !1, e.err2005 = !0, e.step = 1, clearTimeout(s)
            }, 3e3); else 2002 == r.code ? (e.fullscreenLoading = !1, e.registerErr = !0, e.registerTitle = "该邮箱已注册,可直接登录") : (e.fullscreenLoading = !1, e.registerErr = !0, e.registerTitle = "注册失败")
          }))
        }, goLogin: function () {
          this.err2005 = !1, this.$router.push({path: "/Login?login=1"})
        }, goRegister: function () {
          this.err2005 = !1, this.$router.push({path: "/Login?login=0"})
        }
      }, components: {}, watch: {$route: "routeChange"}, created: function () {
        this.routeChange()
      }
    }, o = {
      render: function () {
        var e = this, r = e.$createElement, s = e._self._c || r;
        return s("div", [s("div", {staticClass: "container"}, [s("h1", {staticClass: "loginTitle"}, [s("a", {attrs: {href: "#/"}}, [e._v(e._s(0 != this.$store.state.themeObj.user_start ? "Aimee 的博客" : "Qinlh 的博客"))])]), e._v(" "), s("div", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: !e.err2005,
            expression: "!err2005"
          }]
        }, [1 == e.login ? s("div", {staticClass: "loginBox"}, [e._m(0), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.loginErr,
            expression: "loginErr"
          }], attrs: {title: e.loginTitle, type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "email", placeholder: "邮箱"},
          model: {
            value: e.email, callback: function (r) {
              e.email = r
            }, expression: "email"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.emailErr,
            expression: "emailErr"
          }], attrs: {title: "请输入邮箱", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "password", placeholder: "密码"}, nativeOn: {
            keyup: function (r) {
              return "button" in r || !e._k(r.keyCode, "enter", 13, r.key, "Enter") ? e.loginEnterFun(r) : null
            }
          }, model: {
            value: e.password, callback: function (r) {
              e.password = r
            }, expression: "password"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.passwordErr,
            expression: "passwordErr"
          }], attrs: {title: "请输入密码", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), e._m(1), e._v(" "), s("div", {
          staticClass: "lr-btn tcolors-bg",
          on: {click: e.gotoHome}
        }, [e._v("登录")]), e._v(" "), e._m(2)], 1) : s("div", {staticClass: "registerBox"}, [e._m(3), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.registerErr,
            expression: "registerErr"
          }], attrs: {title: e.registerTitle, type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "text", placeholder: "用户名"},
          model: {
            value: e.nusername, callback: function (r) {
              e.nusername = r
            }, expression: "nusername"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.nusernameErr,
            expression: "nusernameErr"
          }], attrs: {title: "用户名错误", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "email", placeholder: "邮箱"},
          model: {
            value: e.nemail, callback: function (r) {
              e.nemail = r
            }, expression: "nemail"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.nemailErr,
            expression: "nemailErr"
          }], attrs: {title: "邮箱错误", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "password", placeholder: "密码:6-12位英文、数字、下划线"},
          model: {
            value: e.npassword, callback: function (r) {
              e.npassword = r
            }, expression: "npassword"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.npasswordErr,
            expression: "npasswordErr"
          }], attrs: {title: "密码错误", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("el-input", {
          attrs: {type: "password", placeholder: "确认密码"},
          nativeOn: {
            keyup: function (r) {
              return "button" in r || !e._k(r.keyCode, "enter", 13, r.key, "Enter") ? e.registerEnterFun(r) : null
            }
          },
          model: {
            value: e.npassword2, callback: function (r) {
              e.npassword2 = r
            }, expression: "npassword2"
          }
        }), e._v(" "), s("el-alert", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.npassword2Err,
            expression: "npassword2Err"
          }], attrs: {title: "重复密码有误", type: "error", "show-icon": "", closable: !1}
        }), e._v(" "), s("div", {
          directives: [{
            name: "loading",
            rawName: "v-loading.fullscreen.lock",
            value: e.fullscreenLoading,
            expression: "fullscreenLoading",
            modifiers: {fullscreen: !0, lock: !0}
          }], staticClass: "lr-btn tcolors-bg", attrs: {"element-loading-text": "提交中"}, on: {click: e.newRegister}
        }, [e._v("注册")])], 1)]), e._v(" "), s("div", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: e.err2005,
            expression: "err2005"
          }], staticClass: "registerSuc"
        }, [s("div", {staticClass: "sucIcon"}, [s("el-steps", {
          attrs: {
            space: 100,
            active: e.step,
            "finish-status": "success"
          }
        }, [s("el-step", {attrs: {title: "注册"}}), e._v(" "), s("el-step", {attrs: {title: "验证"}}), e._v(" "), s("el-step", {attrs: {title: "登录"}})], 1)], 1), e._v(" "), s("div", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: 0 == e.urlstate,
            expression: "urlstate==0"
          }], staticClass: "sucContent"
        }, [e._v("\n                账号激活链接已发送至您的邮箱：" + e._s(e.nemail) + "\n                "), s("p", [e._v("请您在24小时内登录邮箱，按邮件中的提示完成账号激活操作")])]), e._v(" "), s("div", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: "urlInvalid" == e.urlstate,
            expression: "urlstate=='urlInvalid'"
          }], staticClass: "sucContent"
        }, [e._v("\n                账号已激活，现在去登录   "), s("span", {
          staticClass: "tcolors-bg lastbtn",
          on: {click: e.goLogin}
        }, [e._v("登录")])]), e._v(" "), s("div", {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: "urlErr" == e.urlstate,
            expression: "urlstate=='urlErr'"
          }], staticClass: "sucContent"
        }, [e._v("\n                OwO邮箱激活地址已超时，验证失败，请重新注册   "), s("span", {
          staticClass: "tcolors-bg lastbtn",
          on: {click: e.goRegister}
        }, [e._v("注册")])])])])])
      }, staticRenderFns: [function () {
        var e = this.$createElement, r = this._self._c || e;
        return r("div", {staticClass: "lr-title"}, [r("h1", [this._v("登录")]), this._v(" "), r("p", [this._v("\n                        新用户"), r("a", {
          staticClass: "tcolors",
          attrs: {href: "#/Login?login=0"}
        }, [this._v("注册")])])])
      }, function () {
        var e = this.$createElement, r = this._self._c || e;
        return r("h3", [r("a", {attrs: {href: ""}}, [this._v("忘记密码？")])])
      }, function () {
        var e = this.$createElement, r = this._self._c || e;
        return r("div", {staticClass: "otherLogin"}, [r("a", {attrs: {href: "javascript:void(0)"}}, [r("i", {staticClass: "fa fa-fw fa-wechat"})]), this._v(" "), r("a", {attrs: {href: "javascript:void(0)"}}, [r("i", {staticClass: "fa fa-fw fa-qq"})]), this._v(" "), r("a", {attrs: {href: "javascript:void(0)"}}, [r("i", {staticClass: "fa fa-fw fa-weibo"})])])
      }, function () {
        var e = this.$createElement, r = this._self._c || e;
        return r("div", {staticClass: "lr-title"}, [r("h1", [this._v("注册")]), this._v(" "), r("p", [this._v("\n                        已有账号"), r("a", {
          staticClass: "tcolors",
          attrs: {href: "#/Login?login=1"}
        }, [this._v("登录")])])])
      }]
    };
    var l = s("VU/8")(n, o, !1, function (e) {
      s("tCC8")
    }, null, null);
    r.default = l.exports
  }, tCC8: function (e, r) {
  }
});
