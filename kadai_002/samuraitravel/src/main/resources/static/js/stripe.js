const stripe = Stripe('pk_test_51Prv5n1U0RE6Z8dTzDZXVW96BytygBEDdSXTdfiYkMbEb3zc9zjPo9DtJts9Pu7dzGdFKzRGIex1zkYtujBzS4iN00hMLpDU0S');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });