def decrypt(text):
	a = ord('a')
	z = ord('z')
	RANGE = z-a
	NUMBERS = [
		'zero',
		'one',
		'two',
		'three',
		'four',
		'five',
		'six',
		'seven',
		'eight',
		'nine',
	]
	REFERENCE = "The safe combination is"
	converter = {':':':'}
	offsets = []
	for i in range(len(REFERENCE)):
		r = REFERENCE[i:i+1]
		c = text[i:i+1]
		offset = ord(r)-ord(c)
		if not offset in offsets:
			offsets.append(offset)
		converter[c]=r
	def decrypt_using(c):
		returned = []
		if c in converter:
			returned.append(converter[c])
		else:
			for o in offsets:
				changed = ord(c)+o
				if changed<a:
					changed+=RANGE+1
				if changed>z:
					changed-=RANGE+1
				returned.append(chr(changed))
		return returned
	combination = text[text.index(':')+1:]
	def decode_digit(digit):
		digit = digit.strip()
#		print("decoding digit %s"%digit)
		def in_numbers(n):
#			print("Is %s in NUMBERS ? %s"%(n, n in NUMBERS))
			return n in NUMBERS
		def realize_digits(digits_array, index=0, prefix = ""):
#			print("realizing digits in %s, currently at %d (prefix is %s)"%(digits_array, index, prefix))
			returned = []
			if index<len(digits_array):
				for d in digits_array[index]:
					returned.extend(realize_digits(digits_array, index+1, prefix+d))
			else:
				returned.append(prefix)
			return returned
		digits_array = map(decrypt_using, list(digit))
		potential_digits = realize_digits(list(digits_array))
		filtered_digits = list(filter(in_numbers, potential_digits))
#		print("filtered digits are %s"%filtered_digits)
		return filtered_digits[0]
	def lookup_in_numbers(n):
		return str(NUMBERS.index(n))
	return "".join(map(lookup_in_numbers, map(decode_digit, combination.split("-"))))
	
print(decrypt("Aol zhml jvtipuhapvu pz: zpe-mvby-zpe-mvby-aoyll")=="64643")
print(decrypt("Wkh vdih frpelqdwlrq lv: wkuhh-ilyh-rqh-vla-wzr")=="35162")
print(decrypt("Dro ckpo mywlsxkdsyx sc: pyeb-xsxo-yxo-osqrd-dgy-joby-dgy-yxo-csh-drboo")=="4918202163")
print(decrypt("Ymj xfkj htrgnsfynts nx: ktzw-ejwt-ejwt-ktzw-ktzw-ejwt-jnlmy-xjajs-xjajs")=="400440877")
