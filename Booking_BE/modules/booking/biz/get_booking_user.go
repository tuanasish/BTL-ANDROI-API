// biz/get_user_bookings_biz.go
package biz

import (
	"BOOKING_BE/modules/booking/model"
	"BOOKING_BE/modules/booking/storage"
	"context"
)

type GetUserBookingsBiz struct {
	store *storage.BookingStorage
}

func NewGetUserBookingsBiz(store *storage.BookingStorage) *GetUserBookingsBiz {
	return &GetUserBookingsBiz{store: store}
}

// GetBookingsByUser thực hiện logic lấy danh sách booking của user
func (biz *GetUserBookingsBiz) GetBookingsByUser(ctx context.Context, userID int) ([]model.Booking, error) {
	// Ở đây bạn có thể thêm các xử lý khác như phân trang, lọc theo trạng thái, v.v.
	return biz.store.GetBookingsByUserID(userID)
}
