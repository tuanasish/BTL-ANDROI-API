package transport

import (
	"BOOKING_BE/modules/booking/biz"
	"BOOKING_BE/modules/booking/model"
	"BOOKING_BE/modules/booking/storage"
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func CreateBookingTimeslot(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Bind dữ liệu từ JSON request vào struct BookingTimeSlotRequest
		var data model.BookingTimeSlotRequest
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		// Khởi tạo store và biz
		store := storage.NewBookingTimeSlotStorage(db)
		business := biz.NewCreateBookingTimeslotBiz(store)

		// Gọi hàm nghiệp vụ để tạo đồng thời Booking và TimeSlot
		if err := business.CreateBookingTimeslot(c.Request.Context(), &data); err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về kết quả thành công
		c.JSON(http.StatusCreated, gin.H{"message": true})
	}
}
