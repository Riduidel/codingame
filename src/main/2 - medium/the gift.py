import sys
import math

def share_price_of_gift(participants, gift_price):
	# Sort participants ascending first !
	used_participants = list(participants)
	used_participants.sort()
	contributions = []
	remaining = gift_price
	while used_participants: 
		# At each turn, compute mean contribution and try to see if it fits
		mean_contrib = remaining/len(used_participants)
		p = used_participants.pop(0)
		if remaining>0:
			if remaining>=p:
				contrib = min(p, mean_contrib)
			else:
				contrib = min(p, mean_contrib)
			print >> sys.stderr, "Contributing %d (budget is %d) to remaining %d (contribution mean is %d)"%(contrib, p, remaining, mean_contrib)
			contributions.append(contrib)
			remaining = remaining-contrib
			print >> sys.stderr, "Remaining is now %d"%(remaining)
	if remaining>0:
		raise ValueError("gift price is damn too high !")
	return contributions

def print_shares_for_gift(participants, gift_price):
	try:
		contributions = share_price_of_gift(participants, gift_price)
		for c in contributions:
			print c
	except:
		print "IMPOSSIBLE"

participants_count = int(raw_input())
gift_price = int(raw_input())
participants = []
for i in xrange(participants_count):
    participants.append(int(raw_input()))

print_shares_for_gift(participants, gift_price)
