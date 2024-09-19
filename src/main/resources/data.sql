INSERT INTO public.section(section_name, deleted)
	VALUES ('IELTS',false)
	ON CONFLICT (section_name) DO NOTHING;
INSERT INTO public.section(section_name, deleted)
	VALUES ('PTE',false)
	ON CONFLICT (section_name) DO NOTHING;
INSERT INTO public.section(section_name, deleted)
	VALUES ('OET',false)
	ON CONFLICT (section_name) DO NOTHING;

--INSERT INTO public.course(
--	amount,installment_count, is_installment, section_id, teacher_teacher_id, created_at, update_at, category, created_by, name, updated_by)
--	VALUES (100.00, 2, true, 1, null, '2024-08-19 17:03:27.852801', null, null, 'Speaking', 'IELTS-Speaking', null);
