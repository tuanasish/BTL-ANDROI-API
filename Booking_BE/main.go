package main

import (
	bookingTransport "BOOKING_BE/modules/booking/transport"
	fieldTransport "BOOKING_BE/modules/field/transport" // Đổi tên package field transport
	timeslotTransport "BOOKING_BE/modules/timeslot/transport"
	userTransport "BOOKING_BE/modules/user/transport" // Đổi tên package user transport
	"fmt"
	"log"
	"net/http"
	_ "net/http/pprof"

	"github.com/gin-gonic/gin"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func main() {
	dsn := "root:abc123@tcp(127.0.0.1:3306)/Booking_BE?charset=utf8mb4&parseTime=True&loc=Local"

	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		fmt.Print("Failed to connect to database:", err)
	}

	go func() {
		log.Println(http.ListenAndServe("localhost:6060", nil)) // Chạy pprof trên cổng 6060
	}()

	// Khởi tạo Gin và các route
	router := gin.Default()

	v1 := router.Group("/v1")
	{
		users := v1.Group("/api/auth")
		{
			users.POST("/register", userTransport.Register(db)) // Gọi bằng alias userTransport
			users.GET("/login", userTransport.Login(db))
			users.GET("/:id")
			users.PUT("/update/user_id")
			users.DELETE("/:id")
		}
		fields := v1.Group("/field")
		{
			fields.POST("/create", fieldTransport.CreateField(db))
			fields.GET("/list", fieldTransport.ListField(db)) // Gọi bằng alias fieldTransport
			fields.GET("/list/:field_id/booked", timeslotTransport.GetBookedTimeSlotsByField(db))
			fields.GET("/:id", fieldTransport.GetFieldByID(db))
			fields.PUT("update/:id", fieldTransport.UpdateField(db))
			fields.DELETE("delete/:id", fieldTransport.DeleteField(db))
			fields.GET("/filter", fieldTransport.FilterFields(db))
			fields.GET("/:id/courts", fieldTransport.GetCourtsHandler(db))

		}
		booking := v1.Group("/booking")
		{
			booking.POST("/create", bookingTransport.CreateBookingTimeslot(db))
			booking.GET("/user/:user_id", bookingTransport.GetUserBookings(db)) // Thêm handler cụ thể
			users.PUT("/update/:booking_id", bookingTransport.UpdateBooking(db))
			// booking.GET("/:id", bookingTransport.GetBookingByID(db)) // Thêm endpoint cụ thể
			// booking.PUT("/:id", bookingTransport.UpdateBooking(db)) // Sửa lại
			// booking.DELETE("/:id", bookingTransport.DeleteBooking(db)) // Sửa lại
		}

	}

	// Chạy server
	if err := router.Run(":8000"); err != nil {
		log.Fatal("Server start failed:", err)
	}
}
