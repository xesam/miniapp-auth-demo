const BASE_URL = 'http://127.0.0.1:8081/';

Page({
    data: {
        loginRes: {},
        userInfoRes: {},
        phoneRes: {}
    },
    onLoad(query) {
    },
    onTapLogin(e) {
        const _this = this;
        wx.login({
            success(res) {
                console.log(res);
                if (res.code) {
                    wx.request({
                        url: `${BASE_URL}?action=jscode2session`,
                        data: {
                            code: res.code
                        },
                        success(res) {
                            console.log(res);
                            const data = res.data;
                            _this.setData({
                                loginRes: data
                            });
                        },
                        fail(e) {
                            console.error(e);
                        }
                    });
                } else {
                    console.log('登录失败！' + res.errMsg)
                }
            }
        });
    },
    onGetUserInfo(e) {
        const _this = this;
        console.log('onGetUserInfo', e.detail);
        const {encryptedData, iv} = e.detail;
        wx.request({
            url: `${BASE_URL}?action=getUserInfo`,
            data: {
                encryptedData: encodeURIComponent(encryptedData),
                iv: encodeURIComponent(iv)
            },
            success(res) {
                console.log(res);
                const data = res.data;
                _this.setData({
                    userInfoRes: data
                });
            },
            fail(e) {
                console.error(e);
            }
        });
    },
    onGetPhoneNumber(e) {
        const _this = this;
        console.log('onGetPhoneNumber', e.detail);
        const {encryptedData, iv} = e.detail;
        wx.request({
            url: `${BASE_URL}?action=getPhone`,
            data: {
                encryptedData: encodeURIComponent(encryptedData),
                iv: encodeURIComponent(iv)
            },
            success(res) {
                console.log(res);
                const data = res.data;
                _this.setData({
                    phoneRes: data
                });
            },
            fail(e) {
                console.error(e);
            }
        });
    }
})
