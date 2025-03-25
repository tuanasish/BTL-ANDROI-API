package biz

import (
	"BOOKING_BE/modules/booking/model"
	"context"
	"errors"
)

type CreateBooking interface {
	CreateBookingCourt(*model.Booking) error
}

type CreateBookingBiz struct {
	store CreateBooking
}

func NewCreateBookingBiz(store CreateBooking) *CreateBookingBiz {
	return &CreateBookingBiz{store: store}
}

func (biz *CreateBookingBiz) CreateBookingBiz(ctx context.Context, data *model.Booking) error {
	if data == nil {
		return errors.New("invalid booking data")
	}

	// Xử lý logic nếu cần, kiểm tra ngày giờ hợp lệ
	// if data.StartTime == nil || data.EndTime == nil {
	// 	return errors.New("missing start or end time")
	// }

	// Gọi storage layer để lưu booking
	if err := biz.store.CreateBookingCourt(data); err != nil {
		return err
	}

	return nil
}
