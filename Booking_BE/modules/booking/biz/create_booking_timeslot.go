package biz

import (
	"BOOKING_BE/modules/booking/model"
	"context"
)

type CreateBookingTimeSlot interface {
	CreateBookingTimeSlot(ctx context.Context, data *model.BookingTimeSlotRequest) error
}

type CreateBookingTimeslotBiz struct {
	store CreateBookingTimeSlot
}

func NewCreateBookingTimeslotBiz(store CreateBookingTimeSlot) *CreateBookingTimeslotBiz {
	return &CreateBookingTimeslotBiz{store: store}
}

func (biz *CreateBookingTimeslotBiz) CreateBookingTimeslot(ctx context.Context, data *model.BookingTimeSlotRequest) error {
	if err := biz.store.CreateBookingTimeSlot(ctx, data); err != nil {
		return err
	}
	return nil
}
