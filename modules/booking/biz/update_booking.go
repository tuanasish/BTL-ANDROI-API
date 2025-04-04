package biz

import (
	"BOOKING_BE/modules/booking/model"
	"context"
	"errors"
)

type UpdateBookingStorage interface {
	UpdateBooking(bookingID int, data *model.UpdateBooking) error
}

type updateBookingBiz struct {
	store UpdateBookingStorage
}

func NewUpdateBookingBiz(store UpdateBookingStorage) *updateBookingBiz {
	return &updateBookingBiz{store: store}
}

func (biz *updateBookingBiz) UpdateBookingBiz(ctx context.Context, bookingID int, data *model.UpdateBooking) error {
	// Kiểm tra nếu không có dữ liệu để cập nhật
	if data == nil {
		return errors.New("Không có dữ liệu để cập nhật")
	}

	// Gọi storage layer để cập nhật dữ liệu
	return biz.store.UpdateBooking(bookingID, data)
}
