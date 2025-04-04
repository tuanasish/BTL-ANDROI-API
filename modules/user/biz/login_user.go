package biz

import "BOOKING_BE/modules/user/model"

type LoginUser interface {
	GetUserByEmailPwd(email, pwd string) error
}

type LoginUserBiz struct {
	store LoginUser
}

func NewLoginUserBiz(store LoginUser) *LoginUserBiz {
	return &LoginUserBiz{store: store}
}

func (biz *LoginUserBiz) CheckLogin(email, pwd string) error {
	if email == "" {
		return model.ErrEmail
	}
	if pwd == "" {
		return model.ErrPWD
	}

	if err := biz.store.GetUserByEmailPwd(email, pwd); err != nil {
		return err
	}
	return nil
}
