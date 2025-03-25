package biz

import (
	"BOOKING_BE/modules/timeslot/model"
	"context"
	"errors"
)

// Interface lấy danh sách khung giờ đã đặt cho 1 field cụ thể.
type GetBookedTimeSlotsByField interface {
	GetBookedTimeSlotsByField(ctx context.Context, fieldID int, date string) ([]*model.TimeSlot, error)
}

type GetBookedTimeSlotsBiz struct {
	store GetBookedTimeSlotsByField
}

func NewGetBookedTimeSlotsBiz(store GetBookedTimeSlotsByField) *GetBookedTimeSlotsBiz {
	return &GetBookedTimeSlotsBiz{store: store}
}

func (biz *GetBookedTimeSlotsBiz) GetBookedTimeSlotsByField(ctx context.Context, fieldID int, date string) ([]*model.TimeSlot, error) {
	if fieldID <= 0 {
		return nil, errors.New("field_id không hợp lệ")
	}
	if date == "" {
		return nil, errors.New("ngày không hợp lệ")
	}
	// Gọi storage để lấy danh sách khung giờ đã đặt
	return biz.store.GetBookedTimeSlotsByField(ctx, fieldID, date)
}
